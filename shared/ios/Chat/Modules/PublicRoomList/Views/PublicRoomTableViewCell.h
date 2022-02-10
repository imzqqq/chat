#import <MatrixKit/MatrixKit.h>

@interface PublicRoomTableViewCell : MXKPublicRoomTableViewCell

/**
 Configure the cell in order to display the public room.

 @param publicRoom the public room to render.
 */
- (void)render:(MXPublicRoom*)publicRoom withMatrixSession:(MXSession*)mxSession;

@property (weak, nonatomic) IBOutlet MXKImageView *roomAvatar;

/**
 Get the cell height.
 
 @return the cell height.
 */
+ (CGFloat)cellHeight;

@end
