@protocol ShareItemSenderProtocol;

@class MXRoom;

NS_ASSUME_NONNULL_BEGIN

@protocol ShareItemSenderDelegate

- (void)shareItemSenderDidStartSending:(id<ShareItemSenderProtocol>)shareItemSender;

- (void)shareItemSender:(id<ShareItemSenderProtocol>)shareItemSender didUpdateProgress:(CGFloat)progress;

@end

@protocol ShareItemSenderProtocol <NSObject>

@property (nonatomic, weak) id<ShareItemSenderDelegate> delegate;

- (void)sendItemsToRooms:(NSArray<MXRoom *> *)rooms
                 success:(void(^)(void))success
                 failure:(void(^)(NSArray<NSError *> *))failure;

@end

NS_ASSUME_NONNULL_END
