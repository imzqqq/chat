#import "RoomIncomingTextMsgWithPaginationTitleWithoutSenderNameBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation RoomIncomingTextMsgWithPaginationTitleWithoutSenderNameBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];

    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

@end
