#import <MatrixKit/MatrixKit.h>

/**
 `HomeFilesSearchViewController` displays the files search in user's rooms under a `HomeViewController` segment.
 */
@interface HomeFilesSearchViewController : MXKSearchViewController

/**
 The event selected in the search results
 */
@property (nonatomic, readonly) MXEvent *selectedEvent;

@end
