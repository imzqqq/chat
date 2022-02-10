#import "MessagesSearchResultAttachmentBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"
#import "MXKRoomBubbleTableViewCell+Chat.h"

@implementation MessagesSearchResultAttachmentBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    self.roomNameLabel.textColor = ThemeService.shared.theme.textSecondaryColor;
    
    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
    
    [self updateUserNameColor];
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
            if (!self.roomNameLabel.text.length)
            {
                self.roomNameLabel.text = [MatrixKitL10n roomDisplaynameEmptyRoom];
            }
        }
        else
        {
            self.roomNameLabel.text = bubbleData.roomId;
        }
        
        [self updateUserNameColor];
    }
}

@end
