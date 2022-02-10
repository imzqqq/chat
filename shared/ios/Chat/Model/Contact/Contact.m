#import "Contact.h"

#import "AvatarGenerator.h"

@implementation Contact

- (UIImage*)thumbnailWithPreferedSize:(CGSize)size
{
    UIImage* thumbnail = nil;
    
    // replace the identicon icon by the Vector style one
    if (_mxMember && ([_mxMember.avatarUrl rangeOfString:@"identicon"].location != NSNotFound))
    {        
        thumbnail = [AvatarGenerator generateAvatarForMatrixItem:_mxMember.userId withDisplayName:_mxMember.displayname];
    }
    else
    {
        thumbnail = [super thumbnailWithPreferedSize:size];
    }
    
    // ensure that the thumbnail will have a vector style.
    if (!thumbnail)
    {
        thumbnail = [AvatarGenerator generateAvatarForText:self.displayName];
    }
    
    return thumbnail;
}

@end
