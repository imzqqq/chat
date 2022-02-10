import { AccountSummary, VideoChannelSummary, VideoPlaylist } from '../../Tube/shared/models'
import { AdditionalActorAttributes } from './actor.model'
import { IndexableDoc } from './indexable-doc.model'

export interface IndexablePlaylist extends VideoPlaylist, IndexableDoc {
  url: string
}

export interface DBPlaylist extends Omit<VideoPlaylist, 'isLocal'> {
  indexedAt: Date

  host: string

  // Added by the query
  score?: number

  ownerAccount: AccountSummary & AdditionalActorAttributes
  videoChannel: VideoChannelSummary & AdditionalActorAttributes
}

// Results from the search API
export interface EnhancedPlaylist extends VideoPlaylist {
  score: number
}
