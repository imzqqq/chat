#import "RecentCellData.h"

#import "MXRoom+Chat.h"

#import "GeneratedInterface-Swift.h"

@implementation RecentCellData

//  Adds K handling to super implementation
- (NSString*)notificationCountStringValue
{
    NSString *stringValue;
    NSUInteger notificationCount = self.notificationCount;
    
    if (notificationCount > 1000)
    {
        CGFloat value = notificationCount / 1000.0;
        stringValue = [VectorL10n largeBadgeValueKFormat:value];
    }
    else
    {
        stringValue = [NSString stringWithFormat:@"%tu", notificationCount];
    }
    
    return stringValue;
}

//  Adds mentions-only handling to super implementation
- (NSUInteger)notificationCount
{
    MXRoom *room = [self.mxSession roomWithRoomId:self.roomSummary.roomId];
    // Ignore the regular notification count if the room is in 'mentions only" mode at the Chat level.
    if (room.isMentionsOnly)
    {
        // Only the highlighted missed messages must be considered here.
        return super.highlightCount;
    }
    
    return super.notificationCount;
}

//  Adds "Empty Room" case to super implementation
- (NSString *)roomDisplayname
{
    NSString *result = [super roomDisplayname];
    if (!result.length)
    {
        result = [MatrixKitL10n roomDisplaynameEmptyRoom];
    }
    return result;
}

@end
