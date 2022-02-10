#import <MatrixKit/MatrixKit.h>

/**
 The `ChatSearch` category adds the management of the search bar in Chat screens.
 */

@interface UIViewController (ChatSearch) <UISearchBarDelegate>

/**
 The search bar.
 */
@property (nonatomic, readonly) UISearchBar *searchBar;

/**
 The search bar state.
 */
@property (nonatomic, readonly) BOOL searchBarHidden;

/**
 The Chat empty search background image (champagne bubbles).
 The image adapts its width to its parent view width. 
 Its bottom is aligned to the top of the keyboard.
 */
@property (nonatomic, readonly) UIImageView *backgroundImageView;

@property (nonatomic, readonly) NSLayoutConstraint *backgroundImageViewBottomConstraint;

/**
 Show/Hide the search bar.

 @param animated or not.
 */
- (void)showSearch:(BOOL)animated;
- (void)hideSearch:(BOOL)animated;

/**
 Initialise `backgroundImageView` and add it to the passed parent view.
 
 @param view the view to add `backgroundImageView` to.
 */
- (void)addBackgroundImageViewToView:(UIView*)view;

/**
 Provide the new height of the keyboard to align `backgroundImageView`
 
 @param keyboardHeight the keyboard height.
 */
- (void)setKeyboardHeightForBackgroundImage:(CGFloat)keyboardHeight;

@end
