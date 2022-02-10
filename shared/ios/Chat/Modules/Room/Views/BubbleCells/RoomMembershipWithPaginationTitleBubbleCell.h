#import "RoomMembershipBubbleCell.h"

/**
 `RoomMembershipWithPaginationTitleBubbleCell` displays a membership event with a pagination title.
 */
@interface RoomMembershipWithPaginationTitleBubbleCell : RoomMembershipBubbleCell

@property (weak, nonatomic) IBOutlet UIView *paginationTitleView;
@property (weak, nonatomic) IBOutlet UILabel *paginationLabel;
@property (weak, nonatomic) IBOutlet UIView *paginationSeparatorView;

@end
