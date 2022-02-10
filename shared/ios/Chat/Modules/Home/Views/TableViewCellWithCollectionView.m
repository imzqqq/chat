#import "TableViewCellWithCollectionView.h"
#import "ThemeService.h"
#import "GeneratedInterface-Swift.h"

static CGFloat const kEditionViewCornerRadius = 10.0;

@implementation TableViewCellWithCollectionView

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    self.editionViewHeightConstraint.constant = 0;
    self.editionViewBottomConstraint.constant = 0;
    
    self.editionView.layer.masksToBounds = YES;
}

- (void)customizeTableViewCellRendering
{
    [super customizeTableViewCellRendering];
    
    self.editionView.backgroundColor = ThemeService.shared.theme.headerBackgroundColor;
}

- (void)prepareForReuse
{
    [super prepareForReuse];
    
    self.collectionView.tag = -1;
    self.collectionView.dataSource = nil;
    self.collectionView.delegate = nil;
    
    self.editionViewHeightConstraint.constant = 0;
    self.editionViewBottomConstraint.constant = 0;
    self.editionView.hidden = YES;
    
    self.collectionView.scrollEnabled = YES;
}

- (void)layoutSubviews
{
    [super layoutSubviews];
    
    self.editionView.layer.cornerRadius = kEditionViewCornerRadius;
}

@end

