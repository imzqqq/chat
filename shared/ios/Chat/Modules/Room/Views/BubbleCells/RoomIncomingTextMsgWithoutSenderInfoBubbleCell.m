#import "RoomIncomingTextMsgWithoutSenderInfoBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation RoomIncomingTextMsgWithoutSenderInfoBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];

    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

@end
