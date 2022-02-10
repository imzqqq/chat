#import <MatrixKit/MatrixKit.h>

/**
 This title view display the room display name only.
 There is no user interaction in it except the back button.
 */
@interface SimpleRoomTitleView : MXKRoomTitleView <UIGestureRecognizerDelegate>

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *displayNameCenterXConstraint;

@end