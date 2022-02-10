#import <MatrixKit/MatrixKit.h>

@interface TableViewCellWithLabelAndLargeTextView : MXKTableViewCell <UITextViewDelegate>

@property (strong, nonatomic) IBOutlet UILabel *label;
@property (strong, nonatomic) IBOutlet UITextView *textView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *labelTrailingMinConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *labelLeadingConstraint;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *textViewWidthConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *textViewTrailingConstraint;

@end
