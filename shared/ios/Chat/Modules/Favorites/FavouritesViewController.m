#import "FavouritesViewController.h"

#import "RecentsDataSource.h"
#import "GeneratedInterface-Swift.h"

@interface FavouritesViewController ()
{    
    RecentsDataSource *recentsDataSource;
}

@end

@implementation FavouritesViewController

+ (instancetype)instantiate
{
    UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    FavouritesViewController *viewController = [storyboard instantiateViewControllerWithIdentifier:@"FavouritesViewController"];
    return viewController;
}

- (void)finalizeInit
{
    [super finalizeInit];
    
    self.screenName = @"Favourites";
    
    self.enableDragging = YES;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.view.accessibilityIdentifier = @"FavouritesVCView";
    self.recentsTableView.accessibilityIdentifier = @"FavouritesVCTableView";
    
    // Tag the recents table with the its recents data source mode.
    // This will be used by the shared RecentsDataSource instance for sanity checks (see UITableViewDataSource methods).
    self.recentsTableView.tag = RecentsDataSourceModeFavourites;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [AppDelegate theDelegate].masterTabBarController.navigationItem.title = [VectorL10n titleFavourites];
    [AppDelegate theDelegate].masterTabBarController.tabBar.tintColor = ThemeService.shared.theme.tintColor;
    
    if (recentsDataSource)
    {
        // Take the lead on the shared data source.
        recentsDataSource.areSectionsShrinkable = NO;
        [recentsDataSource setDelegate:self andRecentsDataSourceMode:RecentsDataSourceModeFavourites];
    }
}

- (void)destroy
{
    [super destroy];
}

#pragma mark -

- (void)displayList:(MXKRecentsDataSource *)listDataSource
{
    [super displayList:listDataSource];
    
    // Keep a ref on the recents data source
    if ([listDataSource isKindOfClass:RecentsDataSource.class])
    {
        recentsDataSource = (RecentsDataSource*)listDataSource;
    }
}

#pragma mark - Override RecentsViewController

- (void)refreshCurrentSelectedCell:(BOOL)forceVisible
{
    // Check whether the recents data source is correctly configured.
    if (recentsDataSource.recentsDataSourceMode != RecentsDataSourceModeFavourites)
    {
        return;
    }
    
    [super refreshCurrentSelectedCell:forceVisible];
}

#pragma mark -

- (void)scrollToNextRoomWithMissedNotifications
{
    // Check whether the recents data source is correctly configured.
    if (recentsDataSource.recentsDataSourceMode == RecentsDataSourceModeFavourites)
    {
        [self scrollToTheTopTheNextRoomWithMissedNotificationsInSection:recentsDataSource.favoritesSection];
    }
}

#pragma mark - UITableView delegate

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    // Hide the unique header
    return 0.0f;
}

#pragma mark - Empty view management

- (void)updateEmptyView
{
    [self.emptyView fillWith:[self emptyViewArtwork]
                       title:[VectorL10n favouritesEmptyViewTitle]
             informationText:[VectorL10n favouritesEmptyViewInformation]];
}

- (UIImage*)emptyViewArtwork
{
    if (ThemeService.shared.isCurrentThemeDark)
    {
        return [UIImage imageNamed:@"favourites_empty_screen_artwork_dark"];
    }
    else
    {
        return [UIImage imageNamed:@"favourites_empty_screen_artwork"];
    }
}

@end
