#import <MatrixKit/MatrixKit.h>

/**
 This view controller displays the attachments of a room. Only one matrix session is handled by this view controller.
 */
@interface RoomFilesViewController : MXKRoomViewController

@property (nonatomic) BOOL showCancelBarButtonItem;

@end
