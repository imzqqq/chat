# Chat Client for Android

## Client Features

Features following most important ideas:

- A unified chat list for both direct and group chats
- Optional message bubbles
- Further usability improvements and additional settings to configure the app

## TODOs

- Friends list

## Flow

### Migrate Paging2 to Paging3

- PagedListAdapter -> PagingDataAdapter
- PagingData -> PagedList
- ItemKeyedDataSource, PageKeyedDataSource, PositionalDataSource -> PagingSource 
  - (ItemKeyedDataSource, used in the list of data that is not fixed, such as posts, because the new posts are more frequent, the use of the above two may appear duplicate data, the need for parameters for the entity class unique value and the size of the data volume)
  - (PageKeyedDataSource, suitable for the case of paging by page, which requires a page number and a page data volume size)
  - (PositionalDataSource, suitable for the case of getting data from an arbitrary location, with the starting point and data size as input parameters)
- LivePagedListBuilder, RxPagedListBuilder -> Pager
- PagedList.Config -> PagingConfig
- PagedList.BoundaryCallback -> RemoteMediator
- cachedIn
