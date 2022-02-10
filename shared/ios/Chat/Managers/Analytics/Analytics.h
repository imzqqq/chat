#import <Foundation/Foundation.h>

#import <MatrixSDK/MatrixSDK.h>


// Metrics related to notifications
FOUNDATION_EXPORT NSString *const AnalyticsNoficationsCategory;
FOUNDATION_EXPORT NSString *const AnalyticsNoficationsTimeToDisplayContent;
/**
 The analytics value for accept/decline of the identity server's terms.
 */
FOUNDATION_EXPORT NSString *const AnalyticsContactsIdentityServerAccepted;


/**
 `Analytics` sends analytics to an analytics tool.
 */
@interface Analytics : NSObject <MXAnalyticsDelegate>

/**
 Returns the shared Analytics manager.

 @return the shared Analytics manager.
 */
+ (instancetype)sharedInstance;

/**
 Start doing analytics if the settings `enableCrashReport` is enabled.
 */
- (void)start;

/**
 Stop doing analytics.
 */
- (void)stop;

/**
 Track a screen display.

 @param screenName the name of the displayed screen.
 */
- (void)trackScreen:(NSString*)screenName;

/**
 Flush analytics data.
 */
- (void)dispatch;

@end
