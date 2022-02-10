#import <Foundation/Foundation.h>

/**
 The `RoomEmailInvitation` represents the information extracted from the link in an
 invitation email.
 */
@interface RoomEmailInvitation : NSObject

/**
 The invitation parameters.
 Can be nil.
 */
@property (nonatomic, readonly) NSString *email;
@property (nonatomic, readonly) NSString *signUrl;
@property (nonatomic, readonly) NSString *roomName;
@property (nonatomic, readonly) NSString *roomAvatarUrl;
@property (nonatomic, readonly) NSString *inviterName;
@property (nonatomic, readonly) NSString *guestAccessToken;
@property (nonatomic, readonly) NSString *guestUserId;


/**
 Contructor and parser of the query params of the email link.

 @param params the query parameters extracted from the link.
 */
- (instancetype)initWithParams:(NSDictionary*)params;

@end
