@import Foundation;

#import "EventFormatter.h"

NS_ASSUME_NONNULL_BEGIN

@interface EventFormatter(DTCoreTextFix)

// Fix DTCoreText iOS 13 issue (https://github.com/Cocoanetics/DTCoreText/issues/1168)
+ (void)fixDTCoreTextFont;

@end

NS_ASSUME_NONNULL_END
