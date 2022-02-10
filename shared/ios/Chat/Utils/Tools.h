#import <Foundation/Foundation.h>

#import "MatrixKit/MatrixKit.h"

@interface Tools : NSObject

/**
 Compute the text to display user's presence
 
 @param user the user. Can be nil.
 @return the string to display.
 */
+ (NSString*)presenceText:(MXUser*)user;

#pragma mark - Universal link

/**
 Detect if a URL is a universal link for the application.

 @return YES if the URL can be handled by the app.
 */
+ (BOOL)isUniversalLink:(NSURL*)url;

/**
 Fix a http://vector.im or http://vector.im path url.

 This method fixes the issue with iOS which handles URL badly when there are several hash
 keys ('%23') in the link.
 Vector.im links have often several hash keys...

 @param url a NSURL with possibly several hash keys and thus badly parsed.
 @return a NSURL correctly parsed.
 */
+ (NSURL*)fixURLWithSeveralHashKeys:(NSURL*)url;

#pragma mark - String utilities

/**
 Change the alpha value of all text colors of an attibuted string.

 @param alpha the alpha value to apply.
 @param attributedString the attributed string to update.
 @return a new attributed string.
 */
+ (NSAttributedString *)setTextColorAlpha:(CGFloat)alpha inAttributedString:(NSAttributedString*)attributedString;

@end
