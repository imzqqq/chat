#import "RoomOutgoingTextMsgWithoutSenderInfoBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation RoomOutgoingTextMsgWithoutSenderInfoBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

@end
