#import "RecentsViewController.h"
#import "ContactsDataSource.h"

/**
 'PeopleViewController' instance is used to display/filter the direct rooms and a list of contacts.
 */
@interface PeopleViewController : RecentsViewController

+ (instancetype)instantiate;

/**
 Scroll the next room with missed notifications to the top.
 */
- (void)scrollToNextRoomWithMissedNotifications;

@end

