#import "ContactsTableViewController.h"

/**
 'StartChatViewController' instance is used to prepare new room creation.
 */
@interface StartChatViewController : ContactsTableViewController

/**
 Tell whether a search session is in progress
 */
@property (nonatomic) BOOL isAddParticipantSearchBarEditing;

/**
 Returns the `UINib` object initialized for a `StartChatViewController`.
 
 @return The initialized `UINib` object or `nil` if there were errors during initialization
 or the nib file could not be located.
 */
+ (UINib *)nib;

/**
 Creates and returns a new `StartChatViewController` object.
 
 @discussion This is the designated initializer for programmatic instantiation.
 @return An initialized `StartChatViewController` object if successful, `nil` otherwise.
 */
+ (instancetype)startChatViewController;

@end

