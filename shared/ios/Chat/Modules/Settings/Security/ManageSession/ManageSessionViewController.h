#import <MatrixKit/MatrixKit.h>


@interface ManageSessionViewController : MXKTableViewController

+ (ManageSessionViewController*)instantiateWithMatrixSession:(MXSession*)matrixSession andDevice:(MXDevice*)device;

@end

