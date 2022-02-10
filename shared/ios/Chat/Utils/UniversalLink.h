#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface UniversalLink : NSObject

@property (nonatomic, copy, readonly) NSURL *url;

@property (nonatomic, copy, readonly) NSArray<NSString*> *pathParams;

@property (nonatomic, copy, readonly) NSDictionary<NSString*, NSString*> *queryParams;

- (id)initWithUrl:(NSURL *)url
       pathParams:(NSArray<NSString*> *)pathParams
      queryParams:(NSDictionary<NSString*, NSString*> *)queryParams;

@end

NS_ASSUME_NONNULL_END
