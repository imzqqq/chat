#import "RoomOutgoingAttachmentBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"
#import "MXKRoomBubbleTableViewCell+Chat.h"

@implementation RoomOutgoingAttachmentBubbleCell

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
    [RoomOutgoingAttachmentBubbleCell render:cellData inBubbleCell:self];
}

- (void)didEndDisplay
{
    [super didEndDisplay];
}

+ (void)render:(MXKCellData *)cellData inBubbleCell:(MXKRoomOutgoingAttachmentBubbleCell *)bubbleCell
{
    if (bubbleCell.attachmentView && bubbleCell->bubbleData.isAttachmentWithThumbnail)
    {
        // Show a red border when the attachment sending failed
        if (bubbleCell->bubbleData.attachment.eventSentState == MXEventSentStateFailed)
        {
            bubbleCell.attachmentView.layer.borderColor = ThemeService.shared.theme.warningColor.CGColor;
            bubbleCell.attachmentView.layer.borderWidth = 1;
        }
        else
        {
            bubbleCell.attachmentView.layer.borderWidth = 0;
        }
    }
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
