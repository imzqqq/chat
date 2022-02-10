#import <MatrixKit/MatrixKit.h>

/**
 `MessagesSearchResultAttachmentBubbleCell` displays an attachment with the information of the room and the sender.
 */
@interface MessagesSearchResultAttachmentBubbleCell : MXKRoomBubbleTableViewCell

@property (weak, nonatomic) IBOutlet UIView *roomNameContainerView;
@property (weak, nonatomic) IBOutlet UILabel *roomNameLabel;

@end
