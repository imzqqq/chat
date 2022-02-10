#import "DisabledRoomInputToolbarView.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

@implementation DisabledRoomInputToolbarView

+ (UINib *)nib
{
    return [UINib nibWithNibName:NSStringFromClass([DisabledRoomInputToolbarView class])
                          bundle:[NSBundle bundleForClass:[DisabledRoomInputToolbarView class]]];
}

+ (instancetype)roomInputToolbarView
{
    if ([[self class] nib])
    {
        return [[[self class] nib] instantiateWithOwner:nil options:nil].firstObject;
    }
    else
    {
        return [[self alloc] init];
    }
}

#pragma mark - Override MXKView

-(void)customizeViewRendering
{
    [super customizeViewRendering];
    
    // Remove default toolbar background color
    self.backgroundColor = [UIColor clearColor];
    
    self.separatorView.backgroundColor = ThemeService.shared.theme.lineBreakColor;

    self.disabledReasonTextView.font = [UIFont systemFontOfSize:15];
    self.disabledReasonTextView.textColor = ThemeService.shared.theme.textPrimaryColor;
    self.disabledReasonTextView.tintColor = ThemeService.shared.theme.tintColor;
    self.disabledReasonTextView.editable = NO;
    self.disabledReasonTextView.scrollEnabled = NO;
}

#pragma mark -

- (void)setDisabledReason:(NSString *)reason
{
    self.disabledReasonTextView.text = reason;
}

@end
