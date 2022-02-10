#import <UIKit/UIKit.h>

/**
 Bottom view for SectionHeaderView. Will be insetted respecting safe area insets.
 */
@interface DirectorySectionHeaderContainerView : UIView

/**
 Network label. Both width and height will be used.
 */
@property (nonatomic, strong) UILabel *networkLabel;

/**
 Directory server label. Only height will be used.
 */
@property (nonatomic, strong) UIView *directoryServerLabel;

/**
 Disclosure view. Both width and height will be used.
 */
@property (nonatomic, strong) UIView *disclosureView;

@end
