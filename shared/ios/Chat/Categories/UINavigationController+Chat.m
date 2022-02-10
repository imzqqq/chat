#import "UINavigationController+Chat.h"

@implementation UINavigationController (Chat)

- (BOOL)shouldAutorotate
{
    if (self.topViewController)
    {
        return [self.topViewController shouldAutorotate];
    }
    
    return YES;
}

-(UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    if (self.topViewController)
    {
        return [self.topViewController supportedInterfaceOrientations];
    }
    
    return UIInterfaceOrientationMaskAll;
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation
{
    if (self.topViewController)
    {
        return [self.topViewController preferredInterfaceOrientationForPresentation];
    }
    
    return UIInterfaceOrientationPortrait;
}

@end
