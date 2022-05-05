import { IndexablePlaylist } from 'server/types/playlist.model'
import { ResultList, Video, VideoChannel, VideoDetails, VideoPlaylist } from '@shared/models'
import { doRequestWithRetries } from '../../helpers/requests'
import { INDEXER_COUNT, REQUESTS } from '../../initializers/constants'
import { IndexableChannel } from '../../types/channel.model'
import { IndexableDoc } from '../../types/indexable-doc.model'
import { IndexableVideo } from '../../types/video.model'

async function getVideo (host: string, uuid: string): Promise<IndexableVideo> {
  const url = 'https://' + host + '/api/v1/videos/' + uuid

  const res = await doRequestWithRetries<VideoDetails>({
    uri: url,
    json: true
  }, REQUESTS.MAX_RETRIES, REQUESTS.WAIT)

  return prepareVideoForDB(res.body, host)
}

async function getChannel (host: string, name: string): Promise<IndexableChannel> {
  const url = 'https://' + host + '/api/v1/video-channels/' + name

  const res = await doRequestWithRetries<VideoChannel>({
    uri: url,
    json: true
  }, REQUESTS.MAX_RETRIES, REQUESTS.WAIT)

  const videosCount = await getChannelVideosCount(host, name)

  return prepareChannelForDB(res.body, host, videosCount)
}

async function getChannelVideosCount (host: string, name: string): Promise<number> {
  const url = 'https://' + host + '/api/v1/video-channels/' + name + '/videos'

  const res = await doRequestWithRetries<ResultList<Video>>({
    uri: url,
    qs: {
      start: 0,
      count: 0
    },
    json: true
  }, REQUESTS.MAX_RETRIES, REQUESTS.WAIT)

  const total = res.body?.total
  if (!total || typeof total !== 'number') return 0

  return total
}

async function getVideos (host: string, start: number): Promise<IndexableVideo[]> {
  const url = 'https://' + host + '/api/v1/videos'

  const res = await doRequestWithRetries<ResultList<Video>>({
    uri: url,
    qs: {
      start,
      filter: 'local',
      nsfw: 'both',
      skipCount: true,
      count: INDEXER_COUNT
    },
    json: true
  }, REQUESTS.MAX_RETRIES, REQUESTS.WAIT)

  if (!res.body || !Array.isArray(res.body.data)) {
    throw new Error('Invalid video data from ' + url)
  }

  return res.body.data.map(v => prepareVideoForDB(v, host))
}

async function getPlaylistsOf (host: string, handle: string, start: number): Promise<IndexablePlaylist[]> {
  const url = 'https://' + host + '/api/v1/video-channels/' + handle + '/video-playlists'

  const res = await doRequestWithRetries<ResultList<VideoPlaylist>>({
    uri: url,
    qs: {
      start,
      filter: 'local',
      count: INDEXER_COUNT
    },
    json: true
  }, REQUESTS.MAX_RETRIES, REQUESTS.WAIT)

  if (!res.body || !Array.isArray(res.body.data)) {
    throw new Error('Invalid playlist data from ' + url)
  }

  return res.body.data.map(v => preparePlaylistForDB(v, host))
}

function prepareVideoForDB <T extends Video> (video: T, host: string): T & IndexableDoc {
  return Object.assign(video, {
    elasticSearchId: host + video.id,
    host,
    url: 'https://' + host + '/videos/watch/' + video.uuid
  })
}

function prepareChannelForDB (channel: VideoChannel, host: string, videosCount: number): IndexableChannel {
  return Object.assign(channel, {
    elasticSearchId: host + channel.id,
    host,
    videosCount,
    url: 'https://' + host + '/video-channels/' + channel.name
  })
}

function preparePlaylistForDB (playlist: VideoPlaylist, host: string): IndexablePlaylist {
  return Object.assign(playlist, {
    elasticSearchId: host + playlist.id,
    host,
    url: 'https://' + host + '/videos/watch/playlist/' + playlist.uuid
  })
}

export {
  getVideo,
  getChannel,

  getVideos,
  getPlaylistsOf,

  prepareVideoForDB,
  prepareChannelForDB,
  preparePlaylistForDB
}
