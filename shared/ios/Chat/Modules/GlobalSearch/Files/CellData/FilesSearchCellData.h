#import <MatrixKit/MatrixKit.h>

/**
 `FilesSearchCellData` prepares the data for the Vector cell used to display the files search result.
 */
@interface FilesSearchCellData : MXKCellData <MXKSearchCellDataStoring>
{
    /**
     The data source owner of this instance.
     */
    MXKSearchDataSource *searchDataSource;
}

@end
