#import <UIKit/UIKit.h>

/**
 Section header view class. Respects left and right safe area insets and layouts its subviews.
 */
@interface SectionHeaderView : UIView

/**
 Default value: 20.0
 */
@property (nonatomic, assign) CGFloat minimumLeftInset;

/**
 Default value: 16.0
 */
@property (nonatomic, assign) CGFloat minimumRightInset;

/**
 Default value: 30.0
 */
@property (nonatomic, assign) CGFloat topViewHeight;

/**
 A view which spans the top view. No frame value will be used. Height will be equal to topViewHeight.
 */
@property (nonatomic, strong) UIView *topSpanningView;

/**
 Header label. Only height in frame will be used.
 */
@property (nonatomic, strong) UILabel *headerLabel;

/**
 Accessory view for top view. Both width and height will be used.
 */
@property (nonatomic, strong) UIView *accessoryView;

/**
 A view which spans the bottom view. No frame value will be used. Height will be remaining of the view at below topViewHeight.
 */
@property (nonatomic, strong) UIView *bottomView;

@end
