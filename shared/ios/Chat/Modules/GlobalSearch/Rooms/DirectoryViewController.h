#import <MatrixKit/MatrixKit.h>

@class PublicRoomsDirectoryDataSource;

@interface DirectoryViewController : MXKTableViewController <UITableViewDelegate>

/**
 Display data managed by the passed `PublicRoomsDirectoryDataSource`.

 @param dataSource the data source serving the data.
 */
- (void)displayWitDataSource:(PublicRoomsDirectoryDataSource*)dataSource;

@end
