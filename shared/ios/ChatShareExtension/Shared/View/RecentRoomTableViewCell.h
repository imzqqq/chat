#import <MatrixKit/MatrixKit.h>

@interface RecentRoomTableViewCell : MXKRecentTableViewCell

+ (CGFloat)cellHeight;

- (void)setCustomSelected:(BOOL)selected animated:(BOOL)animated;

@end
