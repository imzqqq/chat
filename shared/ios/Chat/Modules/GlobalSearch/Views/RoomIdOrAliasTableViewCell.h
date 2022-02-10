#import <MatrixKit/MatrixKit.h>

/**
 The `RoomIdOrAliasTableViewCell` cell displays a room identifier or a room alias.
 */
@interface RoomIdOrAliasTableViewCell : MXKTableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;

/**
 Update the information displayed by the cell.
 
 @param roomIdOrAlias the data to render.
 */
- (void)render:(NSString *)roomIdOrAlias;

/**
 Get the cell height.

 @return the cell height.
 */
+ (CGFloat)cellHeight;

@end
