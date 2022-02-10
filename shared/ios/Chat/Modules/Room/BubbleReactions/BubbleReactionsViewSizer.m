#import "BubbleReactionsViewSizer.h"
#import <MatrixSDK/MatrixSDK.h>

#import "GeneratedInterface-Swift.h"

@implementation BubbleReactionsViewSizer

- (CGFloat)heightForViewModel:(BubbleReactionsViewModel*)viewModel
                 fittingWidth:(CGFloat)fittingWidth
{
                            
    CGSize fittingSize = UILayoutFittingCompressedSize;
    fittingSize.width = fittingWidth;
    
    static BubbleReactionsView *bubbleReactionsView;
    
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        bubbleReactionsView = [BubbleReactionsView new];
    });
    
    bubbleReactionsView.frame = CGRectMake(0, 0, fittingWidth, 1.0);
    bubbleReactionsView.viewModel = viewModel;
    [bubbleReactionsView setNeedsLayout];
    [bubbleReactionsView layoutIfNeeded];
            
    return [bubbleReactionsView systemLayoutSizeFittingSize:fittingSize withHorizontalFittingPriority:UILayoutPriorityRequired verticalFittingPriority:UILayoutPriorityFittingSizeLevel].height;        
}

@end
