#import "MessagesSearchResultTextMsgBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"
#import "MXKRoomBubbleTableViewCell+Chat.h"

@implementation MessagesSearchResultTextMsgBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    [self updateUserNameColor];
    
    self.roomNameLabel.textColor = ThemeService.shared.theme.textSecondaryColor;
    
    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

- (void)render:(MXKCellData *)cellData
{
    [super render:cellData];
    
    if (bubbleData)
    {
        MXRoom* room = [bubbleData.mxSession roomWithRoomId:bubbleData.roomId];
        if (room)
        {
            self.roomNameLabel.text = room.summary.displayname;
        }
        else
        {
            self.roomNameLabel.text = bubbleData.roomId;
        }
        
        [self updateUserNameColor];
    }
}

@end
