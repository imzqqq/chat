#import "RecentsViewController.h"

/**
 The `HomeViewController` screen is the main app screen.
 */
@interface HomeViewController : RecentsViewController <UITableViewDataSource, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout>

+ (instancetype)instantiate;

@end
