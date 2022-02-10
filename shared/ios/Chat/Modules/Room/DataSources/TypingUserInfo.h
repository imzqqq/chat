#import <Foundation/Foundation.h>
#import <MatrixSDK/MatrixSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface TypingUserInfo : NSObject

@property (nonatomic, strong) NSString *userId;
@property (nonatomic, strong, nullable) NSString *displayName;
@property (nonatomic, strong, nullable) NSString *avatarUrl;

- (instancetype) initWithMember:(MXRoomMember*)member;

- (instancetype) initWithUserId:(NSString*)userId;

@end

NS_ASSUME_NONNULL_END
