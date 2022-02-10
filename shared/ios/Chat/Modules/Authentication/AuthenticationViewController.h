#import <MatrixKit/MatrixKit.h>

@protocol AuthenticationViewControllerDelegate;


@interface AuthenticationViewController : MXKAuthenticationViewController <MXKAuthenticationViewControllerDelegate>

// MXKAuthenticationViewController has already a `delegate` member
@property (nonatomic, weak) id<AuthenticationViewControllerDelegate> authVCDelegate;

@property (weak, nonatomic) IBOutlet UIView *navigationBackView;
@property (weak, nonatomic) IBOutlet UINavigationBar *navigationBar;
@property (weak, nonatomic) IBOutlet UIView *navigationBarSeparatorView;

@property (weak, nonatomic) IBOutlet UINavigationItem *mainNavigationItem;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *rightBarButtonItem;

@property (weak, nonatomic) IBOutlet UIView *optionsContainer;

@property (weak, nonatomic) IBOutlet UIButton *skipButton;
@property (weak, nonatomic) IBOutlet UIButton *forgotPasswordButton;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *submitButtonMinLeadingConstraint;

@property (weak, nonatomic) IBOutlet UIView *serverOptionsContainer;
@property (weak, nonatomic) IBOutlet UIButton *customServersTickButton;
@property (weak, nonatomic) IBOutlet UIView *customServersContainer;
@property (weak, nonatomic) IBOutlet UIView *homeServerContainer;
@property (weak, nonatomic) IBOutlet UIView *identityServerContainer;

@property (weak, nonatomic) IBOutlet UIView *homeServerSeparator;
@property (weak, nonatomic) IBOutlet UIView *identityServerSeparator;

@property (weak, nonatomic) IBOutlet UIView *softLogoutClearDataContainer;
@property (weak, nonatomic) IBOutlet UILabel *softLogoutClearDataLabel;
@property (weak, nonatomic) IBOutlet UIButton *softLogoutClearDataButton;

- (void)showCustomHomeserver:(NSString*)homeserver andIdentityServer:(NSString*)identityServer;

/// When SSO login succeeded, when SFSafariViewController is used, continue login with success parameters.
/// @param loginToken The login token provided when SSO succeeded.
/// @param txnId transaction id generated during SSO page presentation.
/// returns YES if the SSO login can be continued.
- (BOOL)continueSSOLoginWithToken:(NSString*)loginToken txnId:(NSString*)txnId;

@end


@protocol AuthenticationViewControllerDelegate <NSObject>

- (void)authenticationViewControllerDidDismiss:(AuthenticationViewController *)authenticationViewController;

@end;
