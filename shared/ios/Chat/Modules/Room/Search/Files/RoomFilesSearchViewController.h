#import <MatrixKit/MatrixKit.h>

@interface RoomFilesSearchViewController : MXKSearchViewController

/**
 The event selected in the search results
 */
@property (nonatomic, readonly) MXEvent *selectedEvent;

@end
