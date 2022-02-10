#import <UIKit/UIKit.h>
#import <MatrixKit/MatrixKit.h>

@interface ReadReceiptsViewController : MXKViewController

+ (void)openInViewController:(UIViewController *)viewController fromContainer:(MXKReceiptSendersContainer *)receiptSendersContainer withSession:(MXSession *)session;

@end
