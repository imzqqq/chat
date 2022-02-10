#import "RoomCreationCollapsedBubbleCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

#import "RoomBubbleCellData.h"

@implementation RoomCreationCollapsedBubbleCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];

    self.messageTextView.tintColor = ThemeService.shared.theme.tintColor;
}

@end
