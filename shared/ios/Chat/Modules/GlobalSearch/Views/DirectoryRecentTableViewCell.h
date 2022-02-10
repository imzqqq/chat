#import <MatrixKit/MatrixKit.h>

@class PublicRoomsDirectoryDataSource;

/**
 The `DirectoryRecentTableViewCell` cell displays information about the search on the public
 rooms directory.
 
 It acts as a button to go into the public rooms directory screen.
 */
@interface DirectoryRecentTableViewCell : MXKTableViewCell

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLabel;
@property (weak, nonatomic) IBOutlet UIImageView *chevronImageView;

/**
 Update the information displayed by the cell.

 @param publicRoomsDirectoryDataSource the data to render.
 */
- (void)render:(PublicRoomsDirectoryDataSource *)publicRoomsDirectoryDataSource;

/**
 Get the cell height.

 @return the cell height.
 */
+ (CGFloat)cellHeight;

@end
