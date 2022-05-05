import { NSFWPolicyType } from "../../../shared/models/videos/nsfw-policy.type"

export type InstanceFilters = {
  start: number
  count: number
  sort: string
  signup?: string
  healthy?: string
  nsfwPolicy?: NSFWPolicyType[]
  minUserQuota?: number
  search?: string
  categoriesOr?: number[]
  languagesOr?: string[]
  liveEnabled?: string
}
