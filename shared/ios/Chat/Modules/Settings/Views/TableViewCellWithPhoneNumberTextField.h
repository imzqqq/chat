#import <MatrixKit/MatrixKit.h>

/**
 'TableViewCellWithPhoneNumberTextField' inherits 'MXKTableViewCellWithLabelAndTextField' class.
 It may be used to fill a phone number.
 */
@interface TableViewCellWithPhoneNumberTextField : MXKTableViewCellWithLabelAndTextField
{
}

@property (strong, nonatomic) IBOutlet UIButton *countryCodeButton;
@property (strong, nonatomic) IBOutlet UILabel *isoCountryCodeLabel;

/**
 The current selected country code
 */
@property (nonatomic) NSString *isoCountryCode;

@end
