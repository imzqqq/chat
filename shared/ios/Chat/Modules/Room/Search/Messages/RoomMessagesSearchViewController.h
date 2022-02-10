#import <MatrixKit/MatrixKit.h>

@interface RoomMessagesSearchViewController : MXKSearchViewController

/**
 The event selected in the search results
 */
@property (nonatomic, readonly) MXEvent *selectedEvent;

@end
