#import <MatrixKit/MatrixKit.h>

@class ShareDataSource;

@protocol ShareDataSourceDelegate <NSObject>

- (void)shareDataSourceDidChangeSelectedRoomIdentifiers:(ShareDataSource *)shareDataSource;

@end

@interface ShareDataSource : MXKRecentsDataSource

@property (nonatomic, weak) id<ShareDataSourceDelegate> shareDelegate;

@property (nonatomic, strong, readonly) NSSet<NSString *> *selectedRoomIdentifiers;

- (instancetype)initWithFileStore:(MXFileStore *)fileStore
                      credentials:(MXCredentials *)credentials;

- (void)selectRoomWithIdentifier:(NSString *)roomIdentifier animated:(BOOL)animated;

- (void)deselectRoomWithIdentifier:(NSString *)roomIdentifier animated:(BOOL)animated;

@end
