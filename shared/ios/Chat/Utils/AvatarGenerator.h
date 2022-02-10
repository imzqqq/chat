#import <MatrixKit/MatrixKit.h>

/**
 `AvatarGenerator` class generate an avatar image from objects
 */
@interface AvatarGenerator : NSObject

/**
 Create a squared UIImage with the text and the background color.
 @param text the text.
 @param color the background color.
 @return the avatar image.
 */
+ (UIImage *)imageFromText:(NSString*)text withBackgroundColor:(UIColor*)color;

/**
 Generate a squared avatar for a matrix item (room, room member...).
 @param itemId the matrix identifier of the item
 @param displayname the item displayname (if nil, the itemId is used by default).
 @return the avatar image
 */
+ (UIImage*)generateAvatarForMatrixItem:(NSString*)itemId withDisplayName:(NSString*)displayname;

/**
 Generate a squared avatar for a matrix item (room, room member...) with a preferred size
 @param itemId the matrix identifier of the item
 @param displayname the item displayname (if nil, the itemId is used by default).
 @param size the expected size of the returned image
 @param fontSize the expected font size
 @return the avatar image
 */
+ (UIImage*)generateAvatarForMatrixItem:(NSString*)itemId withDisplayName:(NSString*)displayname size:(CGFloat)size andFontSize:(CGFloat)fontSize;

/**
 Generate an avatar for a text.
 @param text the text.
 @return the avatar image
 */
+ (UIImage*)generateAvatarForText:(NSString*)text;

/**
 Clear all the resources stored in memory.
 */
+ (void)clear;

@end
