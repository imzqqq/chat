import { SORTABLE_COLUMNS } from '../../initializers/constants'
import { checkSort, createSortableColumns } from './utils'

const SORTABLE_VIDEOS_SEARCH_COLUMNS = createSortableColumns(SORTABLE_COLUMNS.VIDEOS_SEARCH)
const SORTABLE_CHANNELS_SEARCH_COLUMNS = createSortableColumns(SORTABLE_COLUMNS.CHANNELS_SEARCH)
const SORTABLE_PLAYLISTS_SEARCH_COLUMNS = createSortableColumns(SORTABLE_COLUMNS.PLAYLISTS_SEARCH)

const videosSearchSortValidator = checkSort(SORTABLE_VIDEOS_SEARCH_COLUMNS)
const channelsSearchSortValidator = checkSort(SORTABLE_CHANNELS_SEARCH_COLUMNS)
const playlistsSearchSortValidator = checkSort(SORTABLE_PLAYLISTS_SEARCH_COLUMNS)

// ---------------------------------------------------------------------------

export {
  videosSearchSortValidator,
  channelsSearchSortValidator,
  playlistsSearchSortValidator
}
