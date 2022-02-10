#import <Foundation/Foundation.h>

/**
 The type of matrix event used for matrix widgets.
 */
FOUNDATION_EXPORT NSString *const kWidgetMatrixEventTypeString;

/**
 The type of matrix event used for modular widgets.
 TODO: It should be replaced by kWidgetMatrixEventTypeString.
 */
FOUNDATION_EXPORT NSString *const kWidgetModularEventTypeString;

/**
 Known types widgets.
 */
FOUNDATION_EXPORT NSString *const kWidgetTypeJitsiV1;
FOUNDATION_EXPORT NSString *const kWidgetTypeJitsiV2;
FOUNDATION_EXPORT NSString *const kWidgetTypeStickerPicker;

@interface WidgetConstants : NSObject

- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)new NS_UNAVAILABLE;

@end
