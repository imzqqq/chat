#import <MatrixKit/MatrixKit.h>

@protocol ShareItemSenderProtocol;

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, ShareManagerType) {
    ShareManagerTypeSend,
    ShareManagerTypeForward,
};

typedef NS_ENUM(NSUInteger, ShareManagerResult) {
    ShareManagerResultFinished,
    ShareManagerResultCancelled,
    ShareManagerResultFailed
};

@interface ShareManager : NSObject

@property (nonatomic, copy) void (^completionCallback)(ShareManagerResult);

- (instancetype)initWithShareItemSender:(id<ShareItemSenderProtocol>)itemSender
                                   type:(ShareManagerType)type;

- (UIViewController *)mainViewController;

@end


NS_ASSUME_NONNULL_END
