#import <MatrixKit/MatrixKit.h>

#import "HomeMessagesSearchDataSource.h"

#import "RoomBubbleCellData.h"

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

#import "MXKRoomBubbleTableViewCell+Chat.h"

@implementation HomeMessagesSearchDataSource

- (void)destroy
{
    [super destroy];
}

- (void)convertHomeserverResultsIntoCells:(MXSearchRoomEventResults *)roomEventResults onComplete:(dispatch_block_t)onComplete
{
    MXKRoomDataSourceManager *roomDataSourceManager = [MXKRoomDataSourceManager sharedManagerForMatrixSession:self.mxSession];

    dispatch_group_t group = dispatch_group_create();
    
    // Convert the HS results into `RoomViewController` cells
    for (MXSearchResult *result in roomEventResults.results)
    {
        // Retrieve the local room data source thanks to the room identifier
        // Note: if no local room data source exist the result is ignored.
        NSString *roomId = result.result.roomId;
        if (roomId)
        {
            dispatch_group_enter(group);

            // Check whether the user knows this room to create the room data source if it doesn't exist.
            [roomDataSourceManager roomDataSourceForRoom:roomId create:[self.mxSession roomWithRoomId:roomId] onComplete:^(MXKRoomDataSource *roomDataSource) {

                if (roomDataSource)
                {
                    // Prepare text font used to highlight the search pattern.
                    UIFont *patternFont = [roomDataSource.eventFormatter bingTextFont];

                    // Let the `RoomViewController` ecosystem do the job
                    // The search result contains only room message events, no state events.
                    // Thus, passing the current room state is not a huge problem. Only
                    // the user display name and his avatar may be wrong.
                    RoomBubbleCellData *cellData = [[RoomBubbleCellData alloc] initWithEvent:result.result andRoomState:roomDataSource.roomState andRoomDataSource:roomDataSource];
                    if (cellData)
                    {
                        // Highlight the search pattern
                        [cellData highlightPatternInTextMessage:self.searchText withForegroundColor:ThemeService.shared.theme.tintColor andFont:patternFont];

                        // Use profile information as data to display
                        MXSearchUserProfile *userProfile = result.context.profileInfo[result.result.sender];
                        cellData.senderDisplayName = userProfile.displayName;
                        cellData.senderAvatarUrl = userProfile.avatarUrl;

                        [self->cellDataArray insertObject:cellData atIndex:0];
                    }
                }

                dispatch_group_leave(group);
            }];
        }
    }

    dispatch_group_notify(group, dispatch_get_main_queue(), ^{

        // In case of successive messages from the same room,
        // we use the pagination flag to display the room name only on the first message.
        NSString *currentRoomId;
        for (RoomBubbleCellData *cellData in self->cellDataArray)
        {
            if (currentRoomId && [currentRoomId isEqualToString:cellData.roomId])
            {
                cellData.isPaginationFirstBubble = NO;
            }
            else
            {
                cellData.isPaginationFirstBubble = YES;
                currentRoomId = cellData.roomId;
            }
        }

        onComplete();
    });
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [super tableView:tableView cellForRowAtIndexPath:indexPath];
    
    // Finalize cell view customization here
    if ([cell isKindOfClass:MXKRoomBubbleTableViewCell.class])
    {
        MXKRoomBubbleTableViewCell *bubbleCell = (MXKRoomBubbleTableViewCell*)cell;
        
        // Display date for each message
        [bubbleCell addDateLabel];
    }
    
    return cell;
}

@end
