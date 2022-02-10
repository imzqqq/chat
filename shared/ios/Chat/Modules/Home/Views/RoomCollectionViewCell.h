#import <MatrixKit/MatrixKit.h>

@class BadgeLabel;

/**
 'RoomCollectionViewCell' class is used to display a room in a collection view.
 */
@interface RoomCollectionViewCell : MXKCollectionViewCell <MXKCellRendering>
{
@protected
    /**
     The current cell data displayed by the collection view cell
     */
    id<MXKRecentCellDataStoring> roomCellData;
}

@property (weak, nonatomic) IBOutlet UILabel *roomTitle;
@property (weak, nonatomic) IBOutlet UILabel *roomTitle1;
@property (weak, nonatomic) IBOutlet UILabel *roomTitle2;

@property (weak, nonatomic) IBOutlet UIView *editionArrowView;

@property (weak, nonatomic) IBOutlet MXKImageView *roomAvatar;
@property (weak, nonatomic) IBOutlet UIImageView *encryptedRoomIcon;

@property (weak, nonatomic) IBOutlet BadgeLabel *badgeLabel;

@property (nonatomic, readonly) NSString *roomId;

@property (nonatomic) NSInteger collectionViewTag; // default is -1

/**
 The default collection view cell size.
 */
+ (CGSize)defaultCellSize;

@end
