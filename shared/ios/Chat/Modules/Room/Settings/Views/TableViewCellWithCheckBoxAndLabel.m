#import "TableViewCellWithCheckBoxAndLabel.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation TableViewCellWithCheckBoxAndLabel

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    _label.textColor = ThemeService.shared.theme.textPrimaryColor;
    self.checkBox.tintColor = ThemeService.shared.theme.tintColor;
}

- (void)setEnabled:(BOOL)enabled
{
    if (enabled)
    {
        _checkBox.image = [UIImage imageNamed:@"selection_tick"];
    }
    else
    {
        _checkBox.image = [UIImage imageNamed:@"selection_untick"];
    }
    
    _enabled = enabled;
}

@end

