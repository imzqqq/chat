#import <MatrixKit/MatrixKit.h>

/**
 `RecentTableViewCell` instances display a room in the context of the recents list.
 */
@interface RecentTableViewCell : MXKRecentTableViewCell

@property (weak, nonatomic) IBOutlet UIView *missedNotifAndUnreadIndicator;
@property (weak, nonatomic) IBOutlet MXKImageView *roomAvatar;
@property (weak, nonatomic) IBOutlet UIImageView *encryptedRoomIcon;

@property (weak, nonatomic) IBOutlet UILabel *missedNotifAndUnreadBadgeLabel;
@property (weak, nonatomic) IBOutlet UIView  *missedNotifAndUnreadBadgeBgView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *missedNotifAndUnreadBadgeBgViewWidthConstraint;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lastEventDecriptionLabelTrailingConstraint;
@property (weak, nonatomic) IBOutlet UIImageView *unsentImageView;

@end
