#import "RoomMembershipExpandedBubbleCell.h"

/**
 `RoomMembershipExpandedWithPaginationTitleBubbleCell` displays the first membership event of series
 that can be collapsable with a pagination title.
 */
@interface RoomMembershipExpandedWithPaginationTitleBubbleCell : RoomMembershipExpandedBubbleCell

@property (weak, nonatomic) IBOutlet UIView *paginationTitleView;
@property (weak, nonatomic) IBOutlet UILabel *paginationLabel;
@property (weak, nonatomic) IBOutlet UIView *paginationSeparatorView;

@end
