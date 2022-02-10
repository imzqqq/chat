#import <MatrixKit/MatrixKit.h>

/**
 'MediaAlbumTableCell' is a base class for displaying a user album.
 */
@interface MediaAlbumTableCell : MXKTableViewCell

@property (strong, nonatomic) IBOutlet UIImageView *albumThumbnail;
@property (weak, nonatomic) IBOutlet UIImageView *bottomLeftIcon;

@property (strong, nonatomic) IBOutlet UILabel *albumDisplayNameLabel;
@property (strong, nonatomic) IBOutlet UILabel *albumCountLabel;

@end

