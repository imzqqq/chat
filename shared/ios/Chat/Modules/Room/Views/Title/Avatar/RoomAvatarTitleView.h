#import <MatrixKit/MatrixKit.h>

@interface RoomAvatarTitleView : MXKRoomTitleView

@property (weak, nonatomic) IBOutlet UIView *roomAvatarMask;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *roomAvatarMaskCenterXConstraint;

@end
