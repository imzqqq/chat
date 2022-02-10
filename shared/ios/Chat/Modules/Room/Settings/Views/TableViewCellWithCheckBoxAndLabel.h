#import <MatrixKit/MatrixKit.h>

@interface TableViewCellWithCheckBoxAndLabel : MXKTableViewCell

@property (strong, nonatomic) IBOutlet UIImageView *checkBox;
@property (strong, nonatomic) IBOutlet UILabel *label;

@property (nonatomic, getter=isEnabled) BOOL enabled;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *checkBoxLeadingConstraint;

@end
