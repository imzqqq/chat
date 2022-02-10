#import "RoomsViewController.h"

#import "RecentsDataSource.h"

#import "GeneratedInterface-Swift.h"

@interface RoomsViewController ()
{
    RecentsDataSource *recentsDataSource;
}

@end

@implementation RoomsViewController

+ (instancetype)instantiate
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    RoomsViewController *viewController = [storyboard instantiateViewControllerWithIdentifier:@"RoomsViewController"];
    return viewController;
}

- (void)finalizeInit
{
    [super finalizeInit];
    
    self.screenName = @"Rooms";
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.view.accessibilityIdentifier = @"RoomsVCView";
    self.recentsTableView.accessibilityIdentifier = @"RoomsVCTableView";
    
    // Tag the recents table with the its recents data source mode.
    // This will be used by the shared RecentsDataSource instance for sanity checks (see UITableViewDataSource methods).
    self.recentsTableView.tag = RecentsDataSourceModeRooms;
    
    // Add the (+) button programmatically
    plusButtonImageView = [self vc_addFABWithImage:[UIImage imageNamed:@"rooms_floating_action"]
                                            target:self
                                            action:@selector(onPlusButtonPressed)];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [AppDelegate theDelegate].masterTabBarController.navigationItem.title = [VectorL10n titleRooms];
    [AppDelegate theDelegate].masterTabBarController.tabBar.tintColor = ThemeService.shared.theme.tintColor;
    
    if ([self.dataSource isKindOfClass:RecentsDataSource.class])
    {
        // Take the lead on the shared data source.
        recentsDataSource = (RecentsDataSource*)self.dataSource;
        recentsDataSource.areSectionsShrinkable = NO;
        [recentsDataSource setDelegate:self andRecentsDataSourceMode:RecentsDataSourceModeRooms];
    }
}

- (void)destroy
{
    [super destroy];
}

#pragma mark - Override RecentsViewController

- (void)refreshCurrentSelectedCell:(BOOL)forceVisible
{
    // Check whether the recents data source is correctly configured.
    if (recentsDataSource.recentsDataSourceMode != RecentsDataSourceModeRooms)
    {
        return;
    }
    
    [super refreshCurrentSelectedCell:forceVisible];
}

- (void)onPlusButtonPressed
{
    [self showRoomDirectory];
}

#pragma mark - UITableView delegate

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if ([tableView numberOfSections] <= 1)
    {
        // Hide the header to merge Invites and Rooms into a single list.
        return 0.0;
    }
    
    return [super tableView:tableView heightForHeaderInSection:section];
}

#pragma mark - 

- (void)scrollToNextRoomWithMissedNotifications
{
    // Check whether the recents data source is correctly configured.
    if (recentsDataSource.recentsDataSourceMode == RecentsDataSourceModeRooms)
    {
        [self scrollToTheTopTheNextRoomWithMissedNotificationsInSection:recentsDataSource.conversationSection];
    }
}

#pragma mark - Empty view management

- (void)updateEmptyView
{
    [self.emptyView fillWith:[self emptyViewArtwork]
                       title:[VectorL10n roomsEmptyViewTitle]
             informationText:[VectorL10n roomsEmptyViewInformation]];
}

- (UIImage*)emptyViewArtwork
{
    if (ThemeService.shared.isCurrentThemeDark)
    {
        return [UIImage imageNamed:@"rooms_empty_screen_artwork_dark"];
    }
    else
    {
        return [UIImage imageNamed:@"rooms_empty_screen_artwork"];
    }
}

@end
