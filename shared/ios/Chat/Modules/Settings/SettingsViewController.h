#import <MatrixKit/MatrixKit.h>

#import "MediaPickerViewController.h"

@interface SettingsViewController : MXKTableViewController<UITextFieldDelegate, MXKCountryPickerViewControllerDelegate, MXKLanguagePickerViewControllerDelegate, MXKDataSourceDelegate>

+ (instancetype)instantiate;

@end

