import UIKit
import Reusable

class PaginationLoadingViewCell: UITableViewCell, NibReusable, Themable {
    
    // MARK: - Properties
    
    @IBOutlet var activityIndicator: UIActivityIndicatorView!
    
    // MARK: - Public
    
    func update(theme: Theme) {
        self.activityIndicator.tintColor = theme.colors.tertiaryContent
        self.activityIndicator.startAnimating()
    }
}
