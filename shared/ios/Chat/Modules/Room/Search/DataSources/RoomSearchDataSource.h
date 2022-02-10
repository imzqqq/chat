#import <MatrixKit/MatrixKit.h>

/**
 `RoomSearchDataSource` overrides `MXKSearchDataSource` to render search results
 into the same cells as `RoomViewController`.
 */
@interface RoomSearchDataSource : MXKSearchDataSource

/**
 Initialize a new `RoomSearchDataSource` instance.
 
 @param roomDataSource a datasource to be able to reuse `RoomViewController` processing and rendering.
 @return the newly created instance.
 */
- (instancetype)initWithRoomDataSource:(MXKRoomDataSource *)roomDataSource;

@end
