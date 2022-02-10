#import <MatrixKit/MatrixKit.h>

/**
 `GroupTableViewCell` instances display a group in the context of the groups list.
 */
@interface GroupTableViewCell : MXKGroupTableViewCell

@property (weak, nonatomic) IBOutlet MXKImageView *groupAvatar;

/**
 The optional unread badge
 */
@property (weak, nonatomic) IBOutlet UILabel *missedNotifAndUnreadBadgeLabel;
@property (weak, nonatomic) IBOutlet UIView  *missedNotifAndUnreadBadgeBgView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *missedNotifAndUnreadBadgeBgViewWidthConstraint;

@end
