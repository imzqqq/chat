#import "RoomMembershipBubbleCell.h"

/**
 Action identifier used when the user tapped on the "collapse" button.
 */
extern NSString *const kRoomMembershipExpandedBubbleCellTapOnCollapseButton;

/**
 `RoomMembershipExpandedBubbleCell` displays the first membership event of series
 that can be collapsable.
 */
@interface RoomMembershipExpandedBubbleCell : RoomMembershipBubbleCell

@property (weak, nonatomic) IBOutlet UIButton *collapseButton;
@property (weak, nonatomic) IBOutlet UIView *separatorView;

@end
