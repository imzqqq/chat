#import "RecentsViewController.h"

/**
 The `RoomsViewController` screen is the view controller displayed when `Rooms` tab is selected.
 */
@interface RoomsViewController : RecentsViewController

+ (instancetype)instantiate;

/**
 Scroll the next room with missed notifications to the top.
 */
- (void)scrollToNextRoomWithMissedNotifications;


@end
