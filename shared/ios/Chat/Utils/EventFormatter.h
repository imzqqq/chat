#import <MatrixKit/MatrixKit.h>

/**
 Link string used in attributed strings to mark a keys re-request action.
 */
FOUNDATION_EXPORT NSString *const EventFormatterOnReRequestKeysLinkAction;

/**
 Parameters separator in the link string.
 */
FOUNDATION_EXPORT NSString *const EventFormatterLinkActionSeparator;

/**
 Link string used in attributed strings to mark an edited event action.
 */
FOUNDATION_EXPORT NSString *const EventFormatterEditedEventLinkAction;

/**
 `EventFormatter` class inherits from `MXKEventFormatter` to define Vector formatting
 */
@interface EventFormatter : MXKEventFormatter

/**
 Add a "(edited)" mention to edited message.
 Default is YES.
 */
@property (nonatomic) BOOL showEditionMention;

/**
 Text color used to display message edited mention.
 Default is `textSecondaryColor`.
 */
@property (nonatomic) UIColor *editionMentionTextColor;

/**
 Text font used to display message edited mention.
 Default is system font 12.
 */
@property (nonatomic) UIFont *editionMentionTextFont;

/**
 String attributes for event timestamp displayed in chat history.
 */
- (NSDictionary*)stringAttributesForEventTimestamp;

@end
