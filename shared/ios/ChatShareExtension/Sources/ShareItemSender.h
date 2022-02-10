#import <UIKit/UIKit.h>

#import "ShareItemSenderProtocol.h"

@class ShareExtensionShareItemProvider;

NS_ASSUME_NONNULL_BEGIN

@interface ShareItemSender : NSObject <ShareItemSenderProtocol>

- (instancetype)initWithRootViewController:(UIViewController *)rootViewController
                         shareItemProvider:(ShareExtensionShareItemProvider *)shareItemProvider;

@end

NS_ASSUME_NONNULL_END
