#import "GroupTableViewCell.h"

/**
 `GroupTableViewCellWithSwitch` instances display a group with a toggle button.
 */
@interface GroupTableViewCellWithSwitch : GroupTableViewCell

@property (strong, nonatomic) IBOutlet UISwitch *toggleButton;

@end
