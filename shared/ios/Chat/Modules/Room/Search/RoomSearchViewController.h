#import <MatrixKit/MatrixKit.h>

#import "SegmentedViewController.h"

@interface RoomSearchViewController : SegmentedViewController

/**
 The room data source concerned by the search session.
 */
@property (nonatomic) MXKRoomDataSource *roomDataSource;

+ (instancetype)instantiate;

@end
