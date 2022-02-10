#import "NSArray+Element.h"

@implementation NSArray (Element)

- (NSArray *)vc_map:(id (^)(id obj))transform
{
    NSMutableArray *result = [NSMutableArray arrayWithCapacity:self.count];
    [self enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop)
    {
        [result addObject:transform(obj)];
    }];
    return result;
}

- (NSArray *)vc_compactMap:(id _Nullable (^)(id obj))transform
{
    NSMutableArray *result = [NSMutableArray arrayWithCapacity:self.count];
    [self enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop)
    {
        id mappedObject = transform(obj);
        if (mappedObject)
        {
            [result addObject:mappedObject];
        }
    }];
    return result;
}

- (NSArray *)vc_flatMap:(NSArray* (^)(id obj))transform
{
    NSMutableArray *result = [NSMutableArray arrayWithCapacity:self.count];
    [self enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop)
    {
        [result addObjectsFromArray:transform(obj)];
    }];
    return result;
}

@end
