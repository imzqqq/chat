#import "DeviceView.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation DeviceView

#pragma mark - Override MXKView

-(void)customizeViewRendering
{
    [super customizeViewRendering];
    
    self.containerView.backgroundColor = ThemeService.shared.theme.headerBackgroundColor;
    self.textView.backgroundColor = ThemeService.shared.theme.backgroundColor;
    self.defaultTextColor = ThemeService.shared.theme.textPrimaryColor;
    self.cancelButton.tintColor = ThemeService.shared.theme.tintColor;
    self.deleteButton.tintColor = ThemeService.shared.theme.tintColor;
    self.renameButton.tintColor = ThemeService.shared.theme.tintColor;
}

@end
