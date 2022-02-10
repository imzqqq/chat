#import "RoomOutgoingTextMsgWithPaginationTitleBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation RoomOutgoingTextMsgWithPaginationTitleBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    self.paginationLabel.textColor = ThemeService.shared.theme.tintColor;
    self.paginationSeparatorView.backgroundColor = ThemeService.shared.theme.tintColor;
}

- (void)render:(MXKCellData *)cellData
{
    [super render:cellData];
    
    if (bubbleData)
    {
        self.paginationLabel.text = [[bubbleData.eventFormatter dateStringFromDate:bubbleData.date withTime:NO] uppercaseString];
    }
}

@end
