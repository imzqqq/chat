#import "UIViewController+ChatSearch.h"

#import <objc/runtime.h>

#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

/**
 `UIViewControllerChatSearchInternals` is the internal single point storage for the search feature.
 
 It hosts all required data so that only one associated object can be used in the category.
 */
@interface UIViewControllerChatSearchInternals : NSObject

// The search bar
@property (nonatomic) UISearchBar *searchBar;
@property (nonatomic) BOOL searchBarHidden;

// Backup of view when displaying search
@property (nonatomic) UIView *backupTitleView;
@property (nonatomic) UIBarButtonItem *backupLeftBarButtonItem;
@property (nonatomic) UIBarButtonItem *backupRightBarButtonItem;

// The Chat empty search background image (champagne bubbles)
@property (nonatomic) UIImageView *backgroundImageView;
@property (nonatomic) NSLayoutConstraint *backgroundImageViewBottomConstraint;

@end

@implementation UIViewControllerChatSearchInternals
@end


#pragma mark - UIViewController+ChatSearch
#pragma mark -

@interface UIViewController ()

// The single associated object hosting all data.
@property(nonatomic) UIViewControllerChatSearchInternals *searchInternals;

@end

@implementation UIViewController (ChatSearch)

- (UISearchBar *)searchBar
{
    return self.searchInternals.searchBar;
}

- (BOOL)searchBarHidden
{
    return self.searchInternals.searchBarHidden;
}

- (UIImageView*)backgroundImageView
{
    return self.searchInternals.backgroundImageView;
}

- (NSLayoutConstraint *)backgroundImageViewBottomConstraint
{
    return self.searchInternals.backgroundImageViewBottomConstraint;
}

- (void)showSearch:(BOOL)animated
{
    if (self.searchInternals.searchBarHidden)
    {
        // Backup screen header before displaying the search bar in it
        self.searchInternals.backupTitleView = self.navigationItem.titleView;
        self.searchInternals.backupLeftBarButtonItem = self.navigationItem.leftBarButtonItem;
        self.searchInternals.backupRightBarButtonItem = self.navigationItem.rightBarButtonItem;
        
        self.searchInternals.searchBarHidden = NO;
        
        // Reset searches
        self.searchBar.text = @"";
        
        // Customize search bar
        [ThemeService.shared.theme applyStyleOnSearchBar:self.searchBar];
        
        // Remove navigation buttons
        self.navigationItem.hidesBackButton = YES;
        self.navigationItem.rightBarButtonItem = nil;
        self.navigationItem.leftBarButtonItem = nil;
        
        // Add the search bar
        self.navigationItem.titleView = self.searchBar;

        self.extendedLayoutIncludesOpaqueBars = YES;
        
        // On iPad, there is no cancel button inside the UISearchBar
        // So, add a classic cancel right bar button
        if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad)
        {
            UIBarButtonItem *cancelButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemCancel target:self action:@selector(onIPadCancelPressed:)];
            [self.navigationItem setRightBarButtonItem: cancelButton animated:YES];
        }
    }
    
    // And display the keyboard
    [self.searchBar becomeFirstResponder];
}

- (void)hideSearch:(BOOL)animated
{
    if (!self.searchInternals.searchBarHidden)
    {
        // Restore the screen header
        self.navigationItem.hidesBackButton = NO;
        self.navigationItem.titleView = self.searchInternals.backupTitleView;
        self.navigationItem.leftBarButtonItem = self.searchInternals.backupLeftBarButtonItem;
        self.navigationItem.rightBarButtonItem = self.searchInternals.backupRightBarButtonItem;
        
        self.searchInternals.searchBarHidden = YES;
    }
}

- (void)addBackgroundImageViewToView:(UIView*)view
{
    UIImage *searchBgImage = [MXKTools paintImage:[UIImage imageNamed:@"search_bg"] withColor:ThemeService.shared.theme.matrixSearchBackgroundImageTintColor];
    UIImageView *backgroundImageView = [[UIImageView alloc] initWithImage:searchBgImage];
    backgroundImageView.translatesAutoresizingMaskIntoConstraints = NO;

    [view addSubview:backgroundImageView];

    // Keep it at left
    NSLayoutConstraint *leftConstraint = [NSLayoutConstraint constraintWithItem:view
                                                                      attribute:NSLayoutAttributeLeading
                                                                      relatedBy:NSLayoutRelationEqual
                                                                         toItem:backgroundImageView
                                                                      attribute:NSLayoutAttributeLeading
                                                                     multiplier:1.0
                                                                     constant:0];
    // Same width as parent
    NSLayoutConstraint *widthConstraint = [NSLayoutConstraint constraintWithItem:view
                                                                       attribute:NSLayoutAttributeWidth
                                                                       relatedBy:NSLayoutRelationEqual
                                                                          toItem:backgroundImageView
                                                                       attribute:NSLayoutAttributeWidth
                                                                      multiplier:1.0f
                                                                        constant:0.0f];

    // Keep the image aspect ratio
    NSLayoutConstraint *aspectRatioConstraint = [NSLayoutConstraint
                                                  constraintWithItem:backgroundImageView
                                                  attribute:NSLayoutAttributeHeight
                                                  relatedBy:NSLayoutRelationEqual
                                                  toItem:backgroundImageView
                                                  attribute:NSLayoutAttributeWidth
                                                  multiplier:(backgroundImageView.frame.size.height / backgroundImageView.frame.size.width)
                                                  constant:0];

    // Set its position according to its bottom
    NSLayoutConstraint *bottomConstraint = [NSLayoutConstraint constraintWithItem:view
                                                                        attribute:NSLayoutAttributeBottom
                                                                        relatedBy:NSLayoutRelationEqual
                                                                           toItem:backgroundImageView
                                                                        attribute:NSLayoutAttributeBottom
                                                                       multiplier:1.0f
                                                                         constant:216];

    [NSLayoutConstraint activateConstraints:@[leftConstraint,
                                              widthConstraint,
                                              aspectRatioConstraint,
                                              bottomConstraint
                                              ]];

    self.searchInternals.backgroundImageView = backgroundImageView;
    self.searchInternals.backgroundImageViewBottomConstraint = bottomConstraint;

    // It will be showed once the keyboard appears
    backgroundImageView.hidden = YES;
}

- (void)setKeyboardHeightForBackgroundImage:(CGFloat)keyboardHeight
{
    // keyboardHeight = 0 means no keyboard
    if (keyboardHeight > 0)
    {
        self.searchInternals.backgroundImageView.hidden = NO;

        // 60 = 18 + 42 from the Chat design
        self.searchInternals.backgroundImageViewBottomConstraint.constant = keyboardHeight - 60;
    }
    else
    {
        // Hide the search
        self.searchInternals.backgroundImageView.hidden = YES;
    }
}

#pragma mark - UISearchBarDelegate

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar2
{
    // "Search" key has been pressed
    [self.searchBar resignFirstResponder];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar2
{
    [self hideSearch:YES];
}

- (BOOL)searchBarShouldEndEditing:(UISearchBar *)searchBar2
{
    // Keep the search bar cancel button enabled even if the keyboard is not displayed
    dispatch_async(dispatch_get_main_queue(), ^{
        for (UIView *subView in self.searchBar.subviews)
        {
            for (UIView *view in subView.subviews)
            {
                if ([view isKindOfClass:[UIButton class]])
                {
                    [(UIButton *)view setEnabled:YES];
                }
            }
        }
    });
    return YES;
}

#pragma mark - Private methods

- (void)onIPadCancelPressed:(id)sender
{
    // Mimic the same behavior as  on iPhones and call the UISearchBar cancel delegate method
    if ([self.searchBar.delegate respondsToSelector:@selector(searchBarCancelButtonClicked:)])
    {
        [self.searchBar.delegate searchBarCancelButtonClicked:self.searchBar];
    }
}

#pragma mark - Internal associated object

- (void)setSearchInternals:(UIViewControllerChatSearchInternals *)searchInternals
{
    objc_setAssociatedObject(self, @selector(searchInternals), searchInternals, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (UIViewControllerChatSearchInternals *)searchInternals
{
    UIViewControllerChatSearchInternals *searchInternals = objc_getAssociatedObject(self, @selector(searchInternals));
    if (!searchInternals)
    {
        // Initialise internal data at the first call
        searchInternals = [[UIViewControllerChatSearchInternals alloc] init];

        UISearchBar *searchBar = [[UISearchBar alloc] init];
        searchBar.showsCancelButton = YES;
        searchBar.delegate = (id<UISearchBarDelegate>)self;
        searchInternals.searchBar = searchBar;

        self.searchInternals = searchInternals;
        self.searchInternals.searchBarHidden = YES;
    }
    return searchInternals;
}

@end
