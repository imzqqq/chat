#import "RoomIncomingTextMsgWithPaginationTitleBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"
#import "MXKRoomBubbleTableViewCell+Chat.h"

@implementation RoomIncomingTextMsgWithPaginationTitleBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    [self updateUserNameColor];
    self.paginationLabel.textColor = ThemeService.shared.theme.tintColor;
    self.paginationSeparatorView.backgroundColor = ThemeService.shared.theme.tintColor;
    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

- (void)render:(MXKCellData *)cellData
{
    [super render:cellData];
    
    if (bubbleData)
    {
        self.paginationLabel.text = [[bubbleData.eventFormatter dateStringFromDate:bubbleData.date withTime:NO] uppercaseString];
        
        [self updateUserNameColor];
    }
}

@end
