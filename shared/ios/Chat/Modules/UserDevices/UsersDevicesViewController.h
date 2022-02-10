#import <MatrixKit/MatrixKit.h>

#import "DeviceTableViewCell.h"

@interface UsersDevicesViewController : MXKViewController <UITableViewDelegate, UITableViewDataSource, DeviceTableViewCellDelegate>

/**
 Display a map of users/devices.

 @param usersDevices the map to display.
 @param mxSession the Matrix session.
 @param onComplete a block called when the user quits the screen
                   doneButtonPressed is:
                       - YES if the user clicked the Done button, meaning he acknowledges all unknown devices.
                       - NO if the user clicked the Cancel button, meaning he prefers to cancel the current request.
 */
- (void)displayUsersDevices:(MXUsersDevicesMap<MXDeviceInfo*>*)usersDevices andMatrixSession:(MXSession*)mxSession onComplete:(void (^)(BOOL doneButtonPressed))onComplete;

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@end
