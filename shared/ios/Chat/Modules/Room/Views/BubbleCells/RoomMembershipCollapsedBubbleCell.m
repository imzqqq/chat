#import "RoomMembershipCollapsedBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

#import "RoomBubbleCellData.h"

#import "AvatarGenerator.h"

@implementation RoomMembershipCollapsedBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];

    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

- (void)layoutSubviews
{
    [super layoutSubviews];

    // Round avatars
    for (UIView *avatarView in self.avatarsView.subviews)
    {
        [avatarView.layer setCornerRadius:avatarView.frame.size.width / 2];
        avatarView.clipsToBounds = YES;
    }
}

- (void)prepareForReuse
{
    [super prepareForReuse];

    // Reset avatars
    for (UIView *avatarView in self.avatarsView.subviews)
    {
        [avatarView removeFromSuperview];
    }
}

- (void)render:(MXKCellData *)cellData
{
    [super render:cellData];

    // Add up to 5 avatars to self.avatarsView
    RoomBubbleCellData *nextBubbleData = (RoomBubbleCellData*)bubbleData;

    do
    {
        MXKImageView *avatarView = [[MXKImageView alloc] initWithFrame:CGRectMake(12 * self.avatarsView.subviews.count, 0, 16, 16)];

        // Handle user's picture by considering it is stored unencrypted on Matrix media repository
        
        // Use the Chat style placeholder
        if (!nextBubbleData.senderAvatarPlaceholder)
        {
            nextBubbleData.senderAvatarPlaceholder = [AvatarGenerator generateAvatarForMatrixItem:nextBubbleData.senderId withDisplayName:nextBubbleData.senderDisplayName];
        }
        
        avatarView.enableInMemoryCache = YES;
        [avatarView setImageURI:nextBubbleData.senderAvatarUrl
                       withType:nil
            andImageOrientation:UIImageOrientationUp
                  toFitViewSize:avatarView.frame.size
                     withMethod:MXThumbnailingMethodCrop
                   previewImage:nextBubbleData.senderAvatarPlaceholder
                   mediaManager:nextBubbleData.mxSession.mediaManager];

        // Clear the default background color of a MXKImageView instance
        avatarView.defaultBackgroundColor = [UIColor clearColor];

        [self.avatarsView addSubview:avatarView];
    }
    while ((nextBubbleData = nextBubbleData.nextCollapsableCellData) && self.avatarsView.subviews.count < 5);
}

@end
