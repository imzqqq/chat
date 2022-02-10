#import <MatrixKit/MatrixKit.h>

/**
 The `RoomTableViewCell` cell displays a room (avatar and displayname).
 */
@interface RoomTableViewCell : MXKTableViewCell

@property (weak, nonatomic) IBOutlet MXKImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *encryptedRoomIcon;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;

/**
 Update the information displayed by the cell.
 
 @param room the room to render.
 */
- (void)render:(MXRoom *)room;

/**
 Get the cell height.

 @return the cell height.
 */
+ (CGFloat)cellHeight;

@end
