#import "MediaAlbumTableCell.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation MediaAlbumTableCell

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    self.albumDisplayNameLabel.textColor = ThemeService.shared.theme.textPrimaryColor;
    self.albumCountLabel.textColor = ThemeService.shared.theme.textSecondaryColor;
}

@end
