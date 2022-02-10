#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@class BubbleReactionsViewModel;

NS_ASSUME_NONNULL_BEGIN

// `BubbleReactionsViewSizer` allows to determine reactions view height for a given viewModel and width.
@interface BubbleReactionsViewSizer : NSObject

// Use Objective-C as workaround as there is an issue affecting UICollectionView sizing. See https://developer.apple.com/forums/thread/105523 for more information.
- (CGFloat)heightForViewModel:(BubbleReactionsViewModel*)viewModel
                 fittingWidth:(CGFloat)fittingWidth;

@end

NS_ASSUME_NONNULL_END
