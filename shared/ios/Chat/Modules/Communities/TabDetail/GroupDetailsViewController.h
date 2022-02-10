#import "SegmentedViewController.h"

@interface GroupDetailsViewController : SegmentedViewController

@property (strong, readonly, nonatomic) MXGroup *group;
@property (strong, readonly, nonatomic) MXSession *mxSession;

/**
 Returns the `UINib` object initialized for a `GroupDetailsViewController`.
 
 @return The initialized `UINib` object or `nil` if there were errors during initialization
 or the nib file could not be located.
 */
+ (UINib *)nib;

/**
 Creates and returns a new `GroupDetailsViewController` object.
 
 @discussion This is the designated initializer for programmatic instantiation.
 @return An initialized `GroupDetailsViewController` object if successful, `nil` otherwise.
 */
+ (instancetype)instantiate;

/**
 Set the group for which the details are displayed.
 Provide the related matrix session.
 
 @param group
 @param mxSession
 */
- (void)setGroup:(MXGroup*)group withMatrixSession:(MXSession*)mxSession;

@end

