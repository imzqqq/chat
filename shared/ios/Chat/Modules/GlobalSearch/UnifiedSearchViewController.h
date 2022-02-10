#import <MatrixKit/MatrixKit.h>

#import "SegmentedViewController.h"

#import "ContactsTableViewController.h"

/**
 The `UnifiedSearchViewController` screen is the global search screen.
 */
@interface UnifiedSearchViewController : SegmentedViewController <UIGestureRecognizerDelegate, ContactsTableViewControllerDelegate>

+ (instancetype)instantiate;

/**
 Open the public rooms directory page.
 It uses the `publicRoomsDirectoryDataSource` managed by the recents view controller data source
 */
- (void)showPublicRoomsDirectory;

/**
 Tell whether an event has been selected from messages or files search tab.
 */
@property (nonatomic, readonly) MXEvent *selectedSearchEvent;
@property (nonatomic, readonly) MXSession *selectedSearchEventSession;

@end
