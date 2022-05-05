export interface SearchUrl {
  search?: string
  nsfw?: string
  host?: string
  publishedDateRange?: string
  durationRange?: string
  categoryOneOf?: number[]
  licenceOneOf?: number[]
  languageOneOf?: string[]

  tagsAllOf?: string[]
  tagsOneOf?: string[]

  isLive?: boolean | string

  sort?: string
  page?: number | string
}
