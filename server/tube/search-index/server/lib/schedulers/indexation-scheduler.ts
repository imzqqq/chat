import * as Bluebird from 'bluebird'
import { IndexablePlaylist } from 'server/types/playlist.model'
import { inspect } from 'util'
import { logger } from '../../helpers/logger'
import { INDEXER_HOST_CONCURRENCY, INDEXER_COUNT, INDEXER_LIMIT, SCHEDULER_INTERVALS_MS } from '../../initializers/constants'
import { IndexableVideo } from '../../types/video.model'
import { buildInstanceHosts } from '../elastic-search/elastic-search-instances'
import { ChannelIndexer } from '../indexers/channel-indexer'
import { PlaylistIndexer } from '../indexers/playlist-indexer'
import { VideoIndexer } from '../indexers/video-indexer'
import { getPlaylistsOf, getVideos } from '../requests/tube-instance'
import { AbstractScheduler } from './abstract-scheduler'

export class IndexationScheduler extends AbstractScheduler {

  private static instance: IndexationScheduler

  protected schedulerIntervalMs = SCHEDULER_INTERVALS_MS.indexation

  private indexedHosts: string[] = []

  private readonly channelIndexer: ChannelIndexer
  private readonly videoIndexer: VideoIndexer
  private readonly playlistIndexer: PlaylistIndexer

  private readonly indexers: [ ChannelIndexer, VideoIndexer, PlaylistIndexer ]

  private constructor () {
    super()

    this.channelIndexer = new ChannelIndexer()
    this.videoIndexer = new VideoIndexer()
    this.playlistIndexer = new PlaylistIndexer()

    this.indexers = [
      this.channelIndexer,
      this.videoIndexer,
      this.playlistIndexer
    ]
  }

  async initIndexes () {
    return Promise.all(this.indexers.map(i => i.initIndex()))
  }

  getIndexedHosts () {
    return this.indexedHosts
  }

  protected async internalExecute () {
    return this.runIndexer()
  }

  private async runIndexer () {
    logger.info('Running indexer.')

    const { indexHosts, removedHosts } = await buildInstanceHosts()
    this.indexedHosts = indexHosts

    for (const o of this.indexers) {
      await o.removeFromHosts(removedHosts)
    }

    await Bluebird.map(indexHosts, async host => {
      try {
        await this.indexHost(host)
      } catch (err) {
        console.error(inspect(err, { depth: 10 }))
        logger.warn({ err: inspect(err) }, 'Cannot index videos from %s.', host)
      }
    }, { concurrency: INDEXER_HOST_CONCURRENCY })

    for (const o of this.indexers) {
      await o.refreshIndex()
    }

    logger.info('Indexer ended.')
  }

  private async indexHost (host: string) {
    const channelsToSync = new Set<string>()
    const existingChannelsId = new Set<number>()
    const existingVideosId = new Set<number>()

    let videos: IndexableVideo[] = []
    let start = 0

    logger.info('Adding video data from %s.', host)

    do {
      logger.debug('Getting video results from %s (from = %d).', host, start)

      videos = await getVideos(host, start)
      logger.debug('Got %d video results from %s (from = %d).', videos.length, host, start)

      start += videos.length

      if (videos.length !== 0) {
        const { created } = await this.videoIndexer.indexElements(videos)

        logger.debug('Indexed %d videos from %s.', videos.length, host)

        // Fetch complete video foreach created video (to get tags)
        for (const c of created) {
          this.videoIndexer.scheduleIndexation(host, c.uuid)
        }
      }

      for (const video of videos) {
        channelsToSync.add(video.channel.name)

        existingChannelsId.add(video.channel.id)
        existingVideosId.add(video.id)
      }
    } while (videos.length === INDEXER_COUNT && start < INDEXER_LIMIT)

    logger.info('Added video data from %s.', host)

    for (const c of channelsToSync) {
      this.channelIndexer.scheduleIndexation(host, c)
    }

    await this.channelIndexer.removeNotExisting(host, existingChannelsId)
    await this.videoIndexer.removeNotExisting(host, existingVideosId)

    await this.indexPlaylists(host, Array.from(channelsToSync))
  }

  private async indexPlaylists (host: string, channelHandles: string[]) {
    const existingPlaylistsId = new Set<number>()

    logger.info('Adding playlist data from %s.', host)

    for (const channelHandle of channelHandles) {
      let playlists: IndexablePlaylist[] = []
      let start = 0

      do {
        logger.debug('Getting playlist results from %s (from = %d, channelHandle = %s).', host, start, channelHandle)

        playlists = await getPlaylistsOf(host, channelHandle, start)
        logger.debug('Got %d playlist results from %s (from = %d, channelHandle = %s).', playlists.length, host, start, channelHandle)

        start += playlists.length

        if (playlists.length !== 0) {
          await this.playlistIndexer.indexElements(playlists)

          logger.debug('Indexed %d playlists from %s.', playlists.length, host)
        }

        for (const playlist of playlists) {
          existingPlaylistsId.add(playlist.id)
        }
      } while (playlists.length === INDEXER_COUNT && start < INDEXER_LIMIT)
    }

    logger.info('Added playlist data from %s.', host)

    await this.playlistIndexer.removeNotExisting(host, existingPlaylistsId)
  }

  static get Instance () {
    return this.instance || (this.instance = new this())
  }
}
