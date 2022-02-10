#import "TypingUserInfo.h"

@implementation TypingUserInfo

- (instancetype) initWithMember:(MXRoomMember*)member
{
    self = [self initWithUserId:member.userId];
    
    if (self)
    {
        self.displayName = member.displayname;
        self.avatarUrl = member.avatarUrl;
    }
    
    return self;
}

- (instancetype) initWithUserId:(NSString*)userId
{
    self = [super init];
    
    if (self)
    {
        self.userId = userId;
    }
    
    return self;
}

@end
