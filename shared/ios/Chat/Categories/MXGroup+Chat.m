#import "MXGroup+Chat.h"

#import "AvatarGenerator.h"

@implementation MXGroup (Chat)

- (void)setGroupAvatarImageIn:(MXKImageView*)mxkImageView matrixSession:(MXSession*)mxSession
{
    // Use the group display name to prepare the default avatar image.
    NSString *avatarDisplayName = self.profile.name;
    UIImage* avatarImage = [AvatarGenerator generateAvatarForMatrixItem:self.groupId withDisplayName:avatarDisplayName];
    
    if (self.profile.avatarUrl && mxSession)
    {
        mxkImageView.enableInMemoryCache = YES;
        
        [mxkImageView setImageURI:self.profile.avatarUrl
                         withType:nil
              andImageOrientation:UIImageOrientationUp
                    toFitViewSize:mxkImageView.frame.size
                       withMethod:MXThumbnailingMethodCrop
                     previewImage:avatarImage
                     mediaManager:mxSession.mediaManager];
    }
    else
    {
        mxkImageView.image = avatarImage;
    }
    
    mxkImageView.contentMode = UIViewContentModeScaleAspectFill;
}

@end
