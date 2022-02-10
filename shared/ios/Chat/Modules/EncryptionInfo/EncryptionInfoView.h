#import <MatrixKit/MatrixKit.h>

/**
 TODO: This view as it is implemented in this class must disappear.
 It should be part of the device verification flow (`DeviceVerificationCoordinator`).
 */
@interface EncryptionInfoView : MXKEncryptionInfoView

/**
 Open the legacy simple verification screen
 */
- (void)displayLegacyVerificationScreen;

@end

