#import "RoomIncomingAttachmentBubbleCell.h"

/**
 `RoomIncomingEncryptedAttachmentBubbleCell` displays incoming encrypted attachment bubbles with sender's information.
 */
@interface RoomIncomingEncryptedAttachmentBubbleCell : RoomIncomingAttachmentBubbleCell

@property (weak, nonatomic) IBOutlet UIImageView *encryptionStatusView;

@end
