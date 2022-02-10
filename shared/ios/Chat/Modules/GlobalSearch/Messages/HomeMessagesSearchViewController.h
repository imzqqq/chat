#import <MatrixKit/MatrixKit.h>

/**
 `HomeMessagesSearchViewController` displays messages search in user's rooms under a `HomeViewController` segment.
 */
@interface HomeMessagesSearchViewController : MXKSearchViewController

/**
 The event selected in the search results
 */
@property (nonatomic, readonly) MXEvent *selectedEvent;

@end
