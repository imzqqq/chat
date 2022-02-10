#import <MatrixKit/MatrixKit.h>

@interface GroupRoomTableViewCell : MXKTableViewCell

@property (weak, nonatomic) IBOutlet UILabel *roomDisplayName;
@property (weak, nonatomic) IBOutlet UILabel *roomTopic;
@property (weak, nonatomic) IBOutlet MXKImageView *roomAvatar;

/**
 Configure the cell in order to display a room of a group.

 @param groupRoom the room to render.
 */
- (void)render:(MXGroupRoom*)groupRoom withMatrixSession:(MXSession*)mxSession;

@end
