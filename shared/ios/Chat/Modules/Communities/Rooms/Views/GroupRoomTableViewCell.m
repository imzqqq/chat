#import "GroupRoomTableViewCell.h"

#import "AvatarGenerator.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation GroupRoomTableViewCell

- (void)awakeFromNib
{
    [super awakeFromNib];

    // Round image view
    [_roomAvatar.layer setCornerRadius:_roomAvatar.frame.size.width / 2];
    _roomAvatar.clipsToBounds = YES;
}

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    self.roomDisplayName.textColor = ThemeService.shared.theme.textPrimaryColor;
    self.roomTopic.textColor = ThemeService.shared.theme.textSecondaryColor;
    
    _roomAvatar.defaultBackgroundColor = [UIColor clearColor];
}

- (void)render:(MXGroupRoom *)groupRoom withMatrixSession:(MXSession*)mxSession
{
    // Set room display name
    self.roomDisplayName.text = groupRoom.name;
    if (!self.roomDisplayName.text)
    {
        self.roomDisplayName.text = groupRoom.canonicalAlias;
    }
    if (!self.roomDisplayName.text)
    {
        self.roomDisplayName.text = groupRoom.roomId;
    }
    
    // Check whether this room has topic
    if (groupRoom.topic)
    {
        _roomTopic.hidden = NO;
        _roomTopic.text = [MXTools stripNewlineCharacters:groupRoom.topic];
    }
    else
    {
        // Hide and fill the label with a fake description to harmonize the height of all the cells.
        // This is a drawback of the self-sizing cell.
        _roomTopic.hidden = YES;
        _roomTopic.text = @"No topic";
    }

    // Set the avatar
    UIImage* avatarImage = [AvatarGenerator generateAvatarForMatrixItem:groupRoom.roomId withDisplayName:self.roomDisplayName.text];

    if (groupRoom.avatarUrl)
    {
        _roomAvatar.enableInMemoryCache = YES;

        [_roomAvatar setImageURI:groupRoom.avatarUrl
                        withType:nil
             andImageOrientation:UIImageOrientationUp
                   toFitViewSize:_roomAvatar.frame.size
                      withMethod:MXThumbnailingMethodCrop
                    previewImage:avatarImage
                    mediaManager:mxSession.mediaManager];
    }
    else
    {
        _roomAvatar.image = avatarImage;
    }
    
    _roomAvatar.contentMode = UIViewContentModeScaleAspectFill;
}

@end
