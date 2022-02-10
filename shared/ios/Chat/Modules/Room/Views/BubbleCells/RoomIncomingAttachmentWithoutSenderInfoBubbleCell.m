#import "RoomIncomingAttachmentWithoutSenderInfoBubbleCell.h"
#import "MXKRoomBubbleTableViewCell+Chat.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation RoomIncomingAttachmentWithoutSenderInfoBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];

    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

+ (CGFloat)heightForCellData:(MXKCellData*)cellData withMaximumWidth:(CGFloat)maxWidth
{
    CGFloat rowHeight = [self attachmentBubbleCellHeightForCellData:cellData withMaximumWidth:maxWidth];
    
    if (rowHeight <= 0)
    {
        rowHeight = [super heightForCellData:cellData withMaximumWidth:maxWidth];
    }
    
    return rowHeight;
}

@end
