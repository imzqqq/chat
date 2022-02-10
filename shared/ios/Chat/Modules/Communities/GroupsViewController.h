#import <MatrixKit/MatrixKit.h>

/**
 The `GroupsViewController` screen is the view controller displayed when `Groups` tab is selected.
 */
@interface GroupsViewController : MXKGroupListViewController <MXKGroupListViewControllerDelegate>
{
@protected
    /**
     The group identifier related to the cell which is in editing mode (if any).
     */
    NSString *editedGroupId;
    
    /**
     Current alert (if any).
     */
    UIAlertController *currentAlert;
    
    /**
     The image view of the (+) button.
     */
    UIImageView* plusButtonImageView;
}

/**
 If YES, the table view will scroll at the top on the next data source refresh.
 It comes back to NO after each refresh.
 */
@property (nonatomic) BOOL shouldScrollToTopOnRefresh;

/**
 Tell whether the search bar at the top of the groups table is enabled. YES by default.
 */
@property (nonatomic) BOOL enableSearchBar;


+ (instancetype)instantiate;

@end
