@import MatrixSDK;

#import "ChatNavigationController.h"

@implementation ChatNavigationController

- (UIViewController *)childViewControllerForStatusBarStyle
{
    return self.topViewController;
}

- (UIViewController *)childViewControllerForStatusBarHidden
{
    return self.topViewController;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    if (self.topViewController)
    {
        return self.topViewController.supportedInterfaceOrientations;
    }
    return [super supportedInterfaceOrientations];
}

- (BOOL)shouldAutorotate
{
    if (self.topViewController)
    {
        return self.topViewController.shouldAutorotate;
    }
    return [super shouldAutorotate];
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation
{
    if (self.topViewController)
    {
        return self.topViewController.preferredInterfaceOrientationForPresentation;
    }
    return [super preferredInterfaceOrientationForPresentation];
}

- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    if ([self.viewControllers indexOfObject:viewController] != NSNotFound)
    {
        MXLogDebug(@"[ChatNavigationController] pushViewController: is pushing same view controller %@\n%@", viewController, [NSThread callStackSymbols]);
        return;
    }
    [super pushViewController:viewController animated:animated];
}

@end
