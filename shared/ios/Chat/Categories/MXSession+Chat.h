#import <Foundation/Foundation.h>

#import <MatrixSDK/MXSession.h>

@class HomeserverConfiguration;

@interface MXSession (Chat)

/**
 The current number of rooms with missed notifications, including the invites.
 */
- (NSUInteger)vc_missedDiscussionsCount;

/**
Return the homeserver configuration based on HS Well-Known or BuildSettings properties according to existing values.
*/
- (HomeserverConfiguration*)vc_homeserverConfiguration;

/**
 Chat version of [MXSession canEnableE2EByDefaultInNewRoomWithUsers:]
 */
- (MXHTTPOperation*)vc_canEnableE2EByDefaultInNewRoomWithUsers:(NSArray<NSString*>*)userIds
                                                         success:(void (^)(BOOL canEnableE2E))success
                                                         failure:(void (^)(NSError *error))failure;

/**
 Indicate YES if secure key backup can be setup
 */
- (BOOL)vc_canSetupSecureBackup;

// TODO: Move to SDK
- (MXRoom*)vc_roomWithIdOrAlias:(NSString*)roomIdOrAlias;

@end
