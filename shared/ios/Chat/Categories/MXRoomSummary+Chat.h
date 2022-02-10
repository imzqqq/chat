#import <MatrixKit/MatrixKit.h>

/**
 RoomEncryptionTrustLevel represents the trust level in an encrypted room.
 */
typedef NS_ENUM(NSUInteger, RoomEncryptionTrustLevel) {
    RoomEncryptionTrustLevelTrusted,
    RoomEncryptionTrustLevelWarning,
    RoomEncryptionTrustLevelNormal,
    RoomEncryptionTrustLevelUnknown
};


/**
 Define a `MXRoomSummary` category at Chat level.
 */
@interface MXRoomSummary (Chat)

@property(nonatomic, readonly) BOOL isJoined;

/**
 Set the room avatar in the dedicated MXKImageView.
 The riot style implies to use in order :
 1 - the default avatar if there is one
 2 - the member avatar for < 3 members rooms
 3 - the first letter of the room name.
 
 @param mxkImageView the destinated MXKImageView.
 */
- (void)setRoomAvatarImageIn:(MXKImageView*)mxkImageView;

/**
 Get the trust level in the room.
 
 @return the trust level.
 */
- (RoomEncryptionTrustLevel)roomEncryptionTrustLevel;

@end
