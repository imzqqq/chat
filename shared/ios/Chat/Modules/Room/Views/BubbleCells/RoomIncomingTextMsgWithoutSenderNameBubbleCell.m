#import "RoomIncomingTextMsgWithoutSenderNameBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation RoomIncomingTextMsgWithoutSenderNameBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];

    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

@end
