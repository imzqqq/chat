#import "RoomOutgoingTextMsgBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"
#import "MXKRoomBubbleTableViewCell+Chat.h"

@implementation RoomOutgoingTextMsgBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    [self updateUserNameColor];
    
    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}


- (void)render:(MXKCellData *)cellData
{
    [super render:cellData];
    
    [self updateUserNameColor];
}

@end
