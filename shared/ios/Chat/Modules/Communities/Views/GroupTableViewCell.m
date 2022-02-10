#import "GroupTableViewCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

#import "MXGroup+Chat.h"

@implementation GroupTableViewCell

#pragma mark - Class methods

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    if (self.missedNotifAndUnreadBadgeBgView)
    {
        // Initialize unread count badge
        [_missedNotifAndUnreadBadgeBgView.layer setCornerRadius:10];
        _missedNotifAndUnreadBadgeBgViewWidthConstraint.constant = 0;
    }
}

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    self.groupName.textColor = ThemeService.shared.theme.textPrimaryColor;
    self.groupDescription.textColor = ThemeService.shared.theme.textSecondaryColor;
    self.memberCount.textColor = ThemeService.shared.theme.textSecondaryColor;
    
    if (self.missedNotifAndUnreadBadgeLabel)
    {
        self.missedNotifAndUnreadBadgeLabel.textColor = ThemeService.shared.theme.baseTextPrimaryColor;
    }
    
    self.groupAvatar.defaultBackgroundColor = [UIColor clearColor];
}

- (void)layoutSubviews
{
    [super layoutSubviews];
    
    // Round image view
    [_groupAvatar.layer setCornerRadius:_groupAvatar.frame.size.width / 2];
    _groupAvatar.clipsToBounds = YES;
}

- (void)render:(MXKCellData *)cellData
{
    [super render:cellData];
    
    if (self.missedNotifAndUnreadBadgeBgView)
    {
        // Hide by default missed notifications and unread widgets
        self.missedNotifAndUnreadBadgeBgView.hidden = YES;
        self.missedNotifAndUnreadBadgeBgViewWidthConstraint.constant = 0;
    }
    
    if (groupCellData)
    {
        [groupCellData.group setGroupAvatarImageIn:self.groupAvatar matrixSession:groupCellData.groupsDataSource.mxSession];
    }
}

// @TODO: Remove this method required by `MXKCellRendering` protocol.
// It is not used for the groups table view.
+ (CGFloat)heightForCellData:(MXKCellData *)cellData withMaximumWidth:(CGFloat)maxWidth
{
    // The height is fixed
    // @TODO change this to support dynamic fonts
    return 74;
}

@end
