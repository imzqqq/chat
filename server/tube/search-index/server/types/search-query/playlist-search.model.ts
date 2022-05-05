import { VideoPlaylistsSearchQuery as PeerTubePlaylistsSearchQuery } from '../../../Tube/shared/models'
import { CommonSearch } from './common-search.model'

export type PlaylistsSearchQuery = PeerTubePlaylistsSearchQuery & CommonSearch
