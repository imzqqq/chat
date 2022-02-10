import {
  VideoChannelsSearchQuery as PeerTubeChannelsSearchQuery
} from '../../../Tube/shared/models/search/video-channels-search-query.model'
import { CommonSearch } from './common-search.model'

export type ChannelsSearchQuery = PeerTubeChannelsSearchQuery & CommonSearch
