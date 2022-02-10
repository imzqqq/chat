import UIKit
import Reusable

final class EditHistoryHeaderView: UITableViewHeaderFooterView, NibLoadable, Reusable, Themable {
    
    // MARK: - Properties
    
    @IBOutlet private weak var dateLabel: UILabel!
    
    // MARK: - Public
    
    func update(theme: Theme) {
        self.contentView.backgroundColor = theme.backgroundColor
        self.dateLabel.textColor = theme.headerTextPrimaryColor
    }
    
    func fill(with dateString: String) {
        self.dateLabel.text = dateString
    }
}
