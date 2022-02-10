#import <MatrixKit/MatrixKit.h>

/**
 'CallViewController' instance displays a call. Only one matrix session is supported by this view controller.
 */
@interface CallViewController : MXKCallViewController

@property (weak, nonatomic) IBOutlet UIButton *chatButton;

@property (weak, nonatomic) IBOutlet UIView *callControlsBackgroundView;

@property (unsafe_unretained, nonatomic) IBOutlet NSLayoutConstraint *callerImageViewWidthConstraint;

//  Effect views
@property (weak, nonatomic) IBOutlet MXKImageView *blurredCallerImageView;

// At the end of call, this flag indicates if the prompt to use the fallback should be displayed
@property (nonatomic) BOOL shouldPromptForStunServerFallback;

@end
