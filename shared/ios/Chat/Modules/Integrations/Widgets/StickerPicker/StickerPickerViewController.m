#import "StickerPickerViewController.h"

#import "IntegrationManagerViewController.h"

#import "GeneratedInterface-Swift.h"

@interface StickerPickerViewController ()

@end

@implementation StickerPickerViewController

- (void)viewDidLoad
{
    [super viewDidLoad];

    self.navigationItem.title = [VectorL10n roomActionSendSticker];

    // Hide back button title
    self.parentViewController.navigationItem.backBarButtonItem =[[UIBarButtonItem alloc] initWithTitle:@"" style:UIBarButtonItemStylePlain target:nil action:nil];

    UIBarButtonItem *editButton = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemEdit target:self action:@selector(onEditButtonPressed)];
    [self.navigationItem setRightBarButtonItem: editButton animated:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

    // Make sure the content is up-to-date when we come back from the sticker picker settings screen
    [webView reload];
}

- (void)onEditButtonPressed
{
    // Show the sticker picker settings screen
    IntegrationManagerViewController *modularVC = [[IntegrationManagerViewController alloc]
                                                   initForMXSession:self.roomDataSource.mxSession
                                                   inRoom:self.roomDataSource.roomId
                                                   screen:[IntegrationManagerViewController screenForWidget:kWidgetTypeStickerPicker]
                                                   widgetId:self.widget.widgetId];

    [self presentViewController:modularVC animated:NO completion:nil];
}

@end
