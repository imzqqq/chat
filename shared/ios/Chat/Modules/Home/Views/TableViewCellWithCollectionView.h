#import <MatrixKit/MatrixKit.h>

@interface TableViewCellWithCollectionView : MXKTableViewCell

@property (strong, nonatomic) IBOutlet UICollectionView *collectionView;

@property (strong, nonatomic) IBOutlet UIView *editionView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *editionViewHeightConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *editionViewBottomConstraint;
@property (weak, nonatomic) IBOutlet UIButton *directChatButton;
@property (weak, nonatomic) IBOutlet UIImageView *directChatImageView;
@property (weak, nonatomic) IBOutlet UIButton *notificationsButton;
@property (weak, nonatomic) IBOutlet UIImageView *notificationsImageView;
@property (weak, nonatomic) IBOutlet UIButton *favouriteButton;
@property (weak, nonatomic) IBOutlet UIImageView *favouriteImageView;
@property (weak, nonatomic) IBOutlet UIButton *priorityButton;
@property (weak, nonatomic) IBOutlet UIImageView *priorityImageView;
@property (weak, nonatomic) IBOutlet UIButton *leaveButton;
@property (weak, nonatomic) IBOutlet UIImageView *leaveImageView;

@end
