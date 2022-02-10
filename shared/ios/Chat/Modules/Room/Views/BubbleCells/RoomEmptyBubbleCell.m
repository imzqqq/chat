#import "RoomEmptyBubbleCell.h"

@implementation RoomEmptyBubbleCell

- (void)prepareForReuse
{
    [super prepareForReuse];

    if (self.heightConstraint != 0)
    {
        self.heightConstraint = 0;
    }
}

@end
