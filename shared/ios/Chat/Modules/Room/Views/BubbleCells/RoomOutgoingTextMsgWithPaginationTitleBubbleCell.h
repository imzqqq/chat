#import "RoomOutgoingTextMsgBubbleCell.h"

/**
 `RoomOutgoingTextMsgWithPaginationTitleBubbleCell` displays outgoing message bubbles with user's picture and pagination title.
 */
@interface RoomOutgoingTextMsgWithPaginationTitleBubbleCell : RoomOutgoingTextMsgBubbleCell

@property (weak, nonatomic) IBOutlet UIView *paginationTitleView;
@property (weak, nonatomic) IBOutlet UILabel *paginationLabel;
@property (weak, nonatomic) IBOutlet UIView *paginationSeparatorView;

@end
