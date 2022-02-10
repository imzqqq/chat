#import "RoomMembershipBubbleCell.h"

/**
 `RoomCreationWithPaginationCollapsedBubbleCell` displays a room creation event with a pagination title.
 */
@interface RoomCreationWithPaginationCollapsedBubbleCell : RoomMembershipBubbleCell

@property (weak, nonatomic) IBOutlet UIView *paginationTitleView;
@property (weak, nonatomic) IBOutlet UILabel *paginationLabel;
@property (weak, nonatomic) IBOutlet UIView *paginationSeparatorView;

@end
