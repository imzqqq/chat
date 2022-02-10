#import "RoomOutgoingAttachmentBubbleCell.h"

/**
 `RoomOutgoingAttachmentWithPaginationTitleBubbleCell` displays outgoing attachment bubbles and pagination title.
 */
@interface RoomOutgoingAttachmentWithPaginationTitleBubbleCell : RoomOutgoingAttachmentBubbleCell

@property (weak, nonatomic) IBOutlet UIView *paginationTitleView;
@property (weak, nonatomic) IBOutlet UILabel *paginationLabel;
@property (weak, nonatomic) IBOutlet UIView *paginationSeparatorView;

@end
