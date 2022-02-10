#import "ChatSplitViewController.h"

@implementation ChatSplitViewController

- (UIViewController *)childViewControllerForStatusBarStyle
{
    return self.viewControllers.firstObject;
}

- (UIViewController *)childViewControllerForStatusBarHidden
{
    return self.viewControllers.firstObject;
}

@end
