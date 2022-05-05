import { Account, VideoChannel, VideoChannelSummary } from '../../Tube/shared/models'
import { ActorImageExtended, AdditionalActorAttributes } from './actor.model'
import { IndexableDoc } from './indexable-doc.model'

export interface IndexableChannel extends VideoChannel, IndexableDoc {
  url: string
}

export interface DBChannel extends Omit<VideoChannel, 'isLocal'> {
  indexedAt: Date
  handle: string
  url: string

  ownerAccount?: Account & AdditionalActorAttributes

  avatar?: ActorImageExtended

  score?: number
}

export interface DBChannelSummary extends VideoChannelSummary {
  indexedAt: Date
}

// Results from the search API
export interface EnhancedVideoChannel extends VideoChannel {
  score: number
}
