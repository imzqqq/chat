#import "RoomMembershipCollapsedBubbleCell.h"

/**
 `RoomMembershipWithPaginationTitleBubbleCell` displays a sum-up of collapsed membership cells with a pagination title.
 */
@interface RoomMembershipCollapsedWithPaginationTitleBubbleCell : RoomMembershipCollapsedBubbleCell

@property (weak, nonatomic) IBOutlet UIView *paginationTitleView;
@property (weak, nonatomic) IBOutlet UILabel *paginationLabel;
@property (weak, nonatomic) IBOutlet UIView *paginationSeparatorView;

@end
