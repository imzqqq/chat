#import <MatrixKit/MatrixKit.h>

@interface Contact : MXKContact

@property (nonatomic) MXRoomMember* mxMember;

@property (nonatomic) MXRoomThirdPartyInvite* mxThirdPartyInvite;

@property (nonatomic) MXGroupUser* mxGroupUser;

@end
