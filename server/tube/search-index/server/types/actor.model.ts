import { ActorImage } from '../../Tube/shared/models'

export type AdditionalActorAttributes = {
  handle: string
  avatar: ActorImageExtended
  url: string
}

export type ActorImageExtended = ActorImage & { url: string }
