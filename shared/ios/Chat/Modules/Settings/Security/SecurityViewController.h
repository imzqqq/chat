#import <MatrixKit/MatrixKit.h>

@interface SecurityViewController : MXKTableViewController

+ (SecurityViewController*)instantiateWithMatrixSession:(MXSession*)matrixSession;

@end

