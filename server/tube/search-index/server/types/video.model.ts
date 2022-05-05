
import { Account, AccountSummary, Video, VideoChannel, VideoChannelSummary, VideoDetails } from '../../Tube/shared/models'
import { AdditionalActorAttributes } from './actor.model'
import { IndexableDoc } from './indexable-doc.model'

export interface IndexableVideo extends Video, IndexableDoc {
}

export interface IndexableVideoDetails extends VideoDetails, IndexableDoc {
}

export interface DBVideoDetails extends Omit<VideoDetails, 'isLocal'> {
  indexedAt: Date
  host: string
  url: string

  account: Account & AdditionalActorAttributes
  channel: VideoChannel & AdditionalActorAttributes

  score?: number
}

export interface DBVideo extends Omit<Video, 'isLocal'> {
  indexedAt: Date
  host: string
  url: string

  account: AccountSummary & AdditionalActorAttributes
  channel: VideoChannelSummary & AdditionalActorAttributes
}

// Results from the search API
export interface EnhancedVideo extends Video {
  tags: VideoDetails['tags']

  score: number
}
