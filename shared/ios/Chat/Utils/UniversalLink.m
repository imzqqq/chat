#import "UniversalLink.h"

@implementation UniversalLink

- (id)initWithUrl:(NSURL *)url pathParams:(NSArray<NSString *> *)pathParams queryParams:(NSDictionary<NSString *,NSString *> *)queryParams
{
    self = [super init];
    if (self)
    {
        _url = url;
        _pathParams = pathParams;
        _queryParams = queryParams;
    }
    return self;
}

- (BOOL)isEqual:(id)other
{
    if (other == self)
        return YES;

    if (![other isKindOfClass:UniversalLink.class])
        return NO;

    UniversalLink *otherLink = (UniversalLink *)other;

    return [_url isEqual:otherLink.url]
        && [_pathParams isEqualToArray:otherLink.pathParams]
        && [_queryParams isEqualToDictionary:otherLink.queryParams];
}

- (NSUInteger)hash
{
    NSUInteger prime = 31;
    NSUInteger result = 1;

    result = prime * result + [_url hash];
    result = prime * result + [_pathParams hash];
    result = prime * result + [_queryParams hash];

    return result;
}

@end
