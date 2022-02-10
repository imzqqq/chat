#import <MatrixKit/MatrixKit.h>

/**
 `HomeMessagesSearchDataSource` overrides `MXKSearchDataSource` to render search results
 by using the same bubble cell as the chat history `RoomViewController`.
 */
@interface HomeMessagesSearchDataSource : MXKSearchDataSource

@end
