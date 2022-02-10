#import "RecentsViewController.h"

/**
 The `FavouritesViewController` screen is the view controller displayed when `Favourites` tab is selected.
 */
@interface FavouritesViewController : RecentsViewController

+ (instancetype)instantiate;

/**
 Scroll the next room with missed notifications to the top.
 */
- (void)scrollToNextRoomWithMissedNotifications;

@end
