#import <MatrixKit/MatrixKit.h>

#import "GroupTableViewCell.h"

/**
 Action identifier used when the user pressed 'preview' button displayed on group invitation.
 
 The `userInfo` dictionary contains an `MXGroup` object under the `kGroupInviteTableViewCellRoomKey` key, representing the room of the invitation.
 */
extern NSString *const kGroupInviteTableViewCellPreviewButtonPressed;

/**
 Action identifier used when the user pressed 'decline' button displayed on group invitation.
 
 The `userInfo` dictionary contains an `MXGroup` object under the `kGroupInviteTableViewCellRoomKey` key, representing the room of the invitation.
 */
extern NSString *const kGroupInviteTableViewCellDeclineButtonPressed;

/**
 Notifications `userInfo` keys
 */
extern NSString *const kGroupInviteTableViewCellRoomKey;

/**
 `GroupInviteTableViewCell` instances display an invite to a group in the context of the groups list.
 */
@interface GroupInviteTableViewCell : GroupTableViewCell

@property (weak, nonatomic) IBOutlet UIButton *leftButton;
@property (weak, nonatomic) IBOutlet UIButton *rightButton;

@property (weak, nonatomic) IBOutlet UIView  *noticeBadgeView;


@end
