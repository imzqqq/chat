#import <MatrixKit/MatrixKit.h>

/**
 The `DirectoryServerTableViewCell` cell displays a server .
 */
@interface DirectoryServerTableViewCell : MXKTableViewCell

@property (weak, nonatomic) IBOutlet MXKImageView *iconImageView;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;

/**
 Update the information displayed by the cell.
 
 @param cellData the data to render.
 */
- (void)render:(id<MXKDirectoryServerCellDataStoring>)cellData;

/**
 Get the cell height.

 @return the cell height.
 */
+ (CGFloat)cellHeight;

@end
