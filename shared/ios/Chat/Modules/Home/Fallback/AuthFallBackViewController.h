#import "WebViewViewController.h"

NS_ASSUME_NONNULL_BEGIN

@class AuthFallBackViewController;
@protocol AuthFallBackViewControllerDelegate

- (void)authFallBackViewController:(AuthFallBackViewController*)authFallBackViewController didLoginWithLoginResponse:(MXLoginResponse*)loginResponse;
- (void)authFallBackViewControllerDidClose:(AuthFallBackViewController*)authFallBackViewController;

@end


/**
 `AuthFallBackViewController` handles the display of a Matrix fallback URL for
 login, registration and Single-Sign-On.
 */
@interface AuthFallBackViewController : WebViewViewController

@property (nonatomic, weak, nullable) id<AuthFallBackViewControllerDelegate> delegate;

@end

NS_ASSUME_NONNULL_END
