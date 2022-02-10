#import "MatrixContactsDataSource.h"

#import "GeneratedInterface-Swift.h"

@implementation MatrixContactsDataSource

- (instancetype)init
{
    self = [super init];
    if (self) {
        hideNonMatrixEnabledContacts = YES;
    }
    return self;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    if (section == filteredLocalContactsSection)
    {
        return [VectorL10n contactsAddressBookSection];
    }
    else
    {
        return [VectorL10n callTransferContactsAll];
    }
}

@end
