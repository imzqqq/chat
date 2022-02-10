import axios from 'axios'
import { ResultList, VideoPlaylistsSearchQuery } from '../../../Tube/shared/models'
import { VideoChannelsSearchQuery } from '../../../Tube/shared/models/search/video-channels-search-query.model'
import { VideosSearchQuery } from '../../../Tube/shared/models/search/videos-search-query.model'
import { EnhancedVideoChannel } from '../../../server/types/channel.model'
import { EnhancedPlaylist } from '../../../server/types/playlist.model'
import { EnhancedVideo } from '../../../server/types/video.model'
import { buildApiUrl } from './utils'

const baseVideosPath = '/api/v1/search/videos'
const baseVideoChannelsPath = '/api/v1/search/video-channels'
const baseVideoPlaylistsPath = '/api/v1/search/video-playlists'

function searchVideos (params: VideosSearchQuery) {
  const options = {
    params
  }

  if (params.search) Object.assign(options.params, { search: params.search })

  return axios.get<ResultList<EnhancedVideo>>(buildApiUrl(baseVideosPath), options)
    .then(res => res.data)
}

function searchVideoChannels (params: VideoChannelsSearchQuery) {
  const options = {
    params
  }

  if (params.search) Object.assign(options.params, { search: params.search })

  return axios.get<ResultList<EnhancedVideoChannel>>(buildApiUrl(baseVideoChannelsPath), options)
    .then(res => res.data)
}

function searchVideoPlaylists (params: VideoPlaylistsSearchQuery) {
  const options = {
    params
  }

  if (params.search) Object.assign(options.params, { search: params.search })

  return axios.get<ResultList<EnhancedPlaylist>>(buildApiUrl(baseVideoPlaylistsPath), options)
    .then(res => res.data)
}

export {
  searchVideos,
  searchVideoChannels,
  searchVideoPlaylists
}
