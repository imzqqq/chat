#import <MatrixKit/MatrixKit.h>

/**
 `RoomIncomingTextMsgWithPaginationTitleBubbleCell` displays incoming message bubbles with sender's information.
 */
@interface RoomIncomingTextMsgWithPaginationTitleBubbleCell : MXKRoomIncomingTextMsgBubbleCell

@property (weak, nonatomic) IBOutlet UIView *paginationTitleView;
@property (weak, nonatomic) IBOutlet UILabel *paginationLabel;
@property (weak, nonatomic) IBOutlet UIView *paginationSeparatorView;

@end
