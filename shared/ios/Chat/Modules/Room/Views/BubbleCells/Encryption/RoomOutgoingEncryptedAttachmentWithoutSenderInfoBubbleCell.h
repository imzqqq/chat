#import "RoomOutgoingAttachmentWithoutSenderInfoBubbleCell.h"

/**
 `RoomOutgoingEncryptedAttachmentWithoutSenderInfoBubbleCell` displays encrypted outgoing attachment with thumbnail, without user's name.
 */
@interface RoomOutgoingEncryptedAttachmentWithoutSenderInfoBubbleCell : RoomOutgoingAttachmentWithoutSenderInfoBubbleCell

@property (weak, nonatomic) IBOutlet UIImageView *encryptionStatusView;

@end
