#import "RoomIncomingEncryptedAttachmentWithPaginationTitleBubbleCell.h"

/**
 `RoomSelectedStickerBubbleCell` is used to display the current selected sticker if any
 */
@interface RoomSelectedStickerBubbleCell : RoomIncomingEncryptedAttachmentWithPaginationTitleBubbleCell

@property (weak, nonatomic) IBOutlet UIView *descriptionContainerView;
@property (weak, nonatomic) IBOutlet UIView *arrowView;
@property (weak, nonatomic) IBOutlet UIView *descriptionView;
@property (weak, nonatomic) IBOutlet UILabel *descriptionLabel;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *userNameLabelTopConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *attachViewLeadingConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *descriptionContainerViewBottomConstraint;

@end
