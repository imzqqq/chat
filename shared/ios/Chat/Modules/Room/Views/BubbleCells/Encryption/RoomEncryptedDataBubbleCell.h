#import <MatrixKit/MatrixKit.h>

/**
 Action identifier used when the user tapped on the marker displayed in front of an encrypted event.
 
 The `userInfo` dictionary contains an `MXEvent` object under the `kMXKRoomBubbleCellEventKey` key, representing the encrypted event.
 */
extern NSString *const kRoomEncryptedDataBubbleCellTapOnEncryptionIcon;

/**
 `RoomEncryptedDataBubbleCell` defines static methods used to handle the encrypted data in bubbles.
 */
@interface RoomEncryptedDataBubbleCell : NSObject

/**
 Return the icon displayed in front of an event in an encrypted room if needed.
 
 @param bubbleComponent the bubble component.
 */
+ (UIImage*)encryptionIconForBubbleComponent:(MXKRoomBubbleComponent *)bubbleComponent;

/**
 Set the encryption status icon in front of each bubble component.
 
 @param bubbleData the bubble cell data
 @param containerView the container view in which the icons will be added.
 */
+ (void)addEncryptionStatusFromBubbleData:(MXKRoomBubbleCellData *)bubbleData inContainerView:(UIView *)containerView;

@end




