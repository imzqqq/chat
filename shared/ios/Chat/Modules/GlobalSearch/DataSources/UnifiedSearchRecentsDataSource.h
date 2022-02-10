#import <MatrixKit/MatrixKit.h>

#import "RecentsDataSource.h"

/**
 'UnifiedSearchRecentsDataSource' class inherits from 'RecentsDataSource' to define the Chat recents source
 used during the unified search on rooms.
 */
@interface UnifiedSearchRecentsDataSource : RecentsDataSource

#pragma mark - Directory handling

/**
 Hide recents. NO by default.
 */
@property (nonatomic) BOOL hideRecents;

@end
