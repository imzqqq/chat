#import <UIKit/UIKit.h>
#import <MessageUI/MessageUI.h>

#import <MatrixKit/MXKResponderRageShaking.h>

@interface RageShakeManager : NSObject <MXKResponderRageShaking>

+ (id)sharedManager;

/**
 Prompt user to report a crash. The alert is presented by the provided view controller.
 
 @param viewController the view controller which presents the alert
 */
- (void)promptCrashReportInViewController:(UIViewController*)viewController;

@end
