#import <MatrixKit/MatrixKit.h>

/**
 `MessagesSearchResultTextMsgBubbleCell` displays a message text with the information of the room and the sender.
 */
@interface MessagesSearchResultTextMsgBubbleCell : MXKRoomBubbleTableViewCell

@property (weak, nonatomic) IBOutlet UIView *roomNameContainerView;
@property (weak, nonatomic) IBOutlet UILabel *roomNameLabel;

@end
