#import "DecryptionFailure.h"

const struct DecryptionFailureReasonStruct DecryptionFailureReason = {
    .unspecified = @"unspecified_error",
    .olmKeysNotSent = @"olm_keys_not_sent_error",
    .olmIndexError = @"olm_index_error",
    .unexpected = @"unexpected_error"
};

@implementation DecryptionFailure

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _ts = [NSDate date].timeIntervalSince1970;
    }
    return self;
}

@end
