import { VideosSearchQuery as PeerTubeVideosSearchQuery } from '../../../Tube/shared/models/search/videos-search-query.model'
import { CommonSearch } from './common-search.model'

export type VideosSearchQuery = Omit<PeerTubeVideosSearchQuery, 'skipCount' | 'filter'> & CommonSearch & { boostLanguages: string[] }
