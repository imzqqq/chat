#import <Foundation/Foundation.h>

/**
 Failure reasons as defined in https://docs.google.com/document/d/1es7cTCeJEXXfRCTRgZerAM2Wg5ZerHjvlpfTW-gsOfI.
 */
struct DecryptionFailureReasonStruct
{
    __unsafe_unretained NSString * const unspecified;
    __unsafe_unretained NSString * const olmKeysNotSent;
    __unsafe_unretained NSString * const olmIndexError;
    __unsafe_unretained NSString * const unexpected;
};
extern const struct DecryptionFailureReasonStruct DecryptionFailureReason;

/**
 `DecryptionFailure` represents a decryption failure.
 */
@interface DecryptionFailure : NSObject

/**
 The id of the event that was unabled to decrypt.
 */
@property (nonatomic) NSString *failedEventId;

/**
 The time the failure has been reported.
 */
@property (nonatomic, readonly) NSTimeInterval ts;

/**
 Decryption failure reason.
 */
@property (nonatomic) NSString *reason;

@end
