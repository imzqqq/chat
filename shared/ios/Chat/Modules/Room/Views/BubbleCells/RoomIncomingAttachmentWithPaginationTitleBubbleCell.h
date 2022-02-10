#import <MatrixKit/MatrixKit.h>

/**
 `RoomIncomingAttachmentWithPaginationTitleBubbleCell` displays incoming attachment bubbles with sender's information and a pagination title.
 */
@interface RoomIncomingAttachmentWithPaginationTitleBubbleCell : MXKRoomIncomingAttachmentBubbleCell

@property (weak, nonatomic) IBOutlet UIView *paginationTitleView;
@property (weak, nonatomic) IBOutlet UILabel *paginationLabel;
@property (weak, nonatomic) IBOutlet UIView *paginationSeparatorView;

@end
