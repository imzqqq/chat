#import <UIKit/UIKit.h>

#import <MatrixKit/MatrixKit.h>

@interface DirectoryServerPickerViewController : MXKTableViewController <MXKDataSourceDelegate, UITableViewDelegate>

/**
 Display data managed by the passed `MXKDirectoryServersDataSource`.

 @param dataSource the data source serving the data.
 @param onComplete a block called when the picker disappears. It provides data about
                   the selected protocol instance or homeserver.
                   Both nil means the user cancelled the picker.
 */
- (void)displayWithDataSource:(MXKDirectoryServersDataSource*)dataSource
                   onComplete:(void (^)(id<MXKDirectoryServerCellDataStoring> cellData))onComplete;

@end

