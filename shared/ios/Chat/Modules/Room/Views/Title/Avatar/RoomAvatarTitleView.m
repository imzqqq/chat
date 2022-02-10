#import "RoomAvatarTitleView.h"

#import "ThemeService.h"

#import "MXRoomSummary+Chat.h"

@implementation RoomAvatarTitleView

+ (UINib *)nib
{
    return [UINib nibWithNibName:NSStringFromClass([self class])
                          bundle:[NSBundle bundleForClass:[self class]]];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
}

- (void)layoutSubviews
{
    [super layoutSubviews];
    
    if (self.superview)
    {
        // Force the title view layout by adding 2 new constraints on the UINavigationBarContentView instance.
        NSLayoutConstraint *topConstraint = [NSLayoutConstraint constraintWithItem:self
                                                                         attribute:NSLayoutAttributeTop
                                                                         relatedBy:NSLayoutRelationEqual
                                                                            toItem:self.superview
                                                                         attribute:NSLayoutAttributeTop
                                                                        multiplier:1.0f
                                                                          constant:0.0f];
        NSLayoutConstraint *centerXConstraint = [NSLayoutConstraint constraintWithItem:self
                                                                             attribute:NSLayoutAttributeCenterX
                                                                             relatedBy:NSLayoutRelationEqual
                                                                                toItem:self.superview
                                                                             attribute:NSLayoutAttributeCenterX
                                                                            multiplier:1.0f
                                                                              constant:0.0f];
        
        [NSLayoutConstraint activateConstraints:@[topConstraint, centerXConstraint]];
        
        // Do not crop the avatar
        self.superview.clipsToBounds = NO;
    }
}

@end
