#import <MatrixKit/MatrixKit.h>

#import "MediaPickerViewController.h"
#import "TableViewCellWithCheckBoxes.h"

/**
 List the settings fields. Used to preselect/edit a field
 */
typedef enum : NSUInteger {
    /**
     Default.
     */
    RoomSettingsViewControllerFieldNone,
    
    /**
     The room name.
     */
    RoomSettingsViewControllerFieldName,
    
    /**
     The room topic.
     */
    RoomSettingsViewControllerFieldTopic,
    
    /**
     The room avatar.
     */
    RoomSettingsViewControllerFieldAvatar
    
} RoomSettingsViewControllerField;

@interface RoomSettingsViewController : MXKRoomSettingsViewController <UITextViewDelegate, UITextFieldDelegate, MXKRoomMemberDetailsViewControllerDelegate, TableViewCellWithCheckBoxesDelegate>

/**
 Select a settings field in order to edit it ('RoomSettingsViewControllerFieldNone' by default).
 */
@property (nonatomic) RoomSettingsViewControllerField selectedRoomSettingsField;

@end

