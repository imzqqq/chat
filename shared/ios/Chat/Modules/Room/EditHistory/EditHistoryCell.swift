import UIKit
import Reusable

final class EditHistoryCell: UITableViewCell, NibReusable, Themable {

    // MARK: - Properties
    
    @IBOutlet private weak var timestampLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    
    // MARK: - Public
    
    func fill(with timeString: String, and attributedMessage: NSAttributedString) {
        self.timestampLabel.text = timeString
        self.messageLabel.attributedText = attributedMessage
    }
    
    func update(theme: Theme) {
        self.backgroundColor = theme.backgroundColor
        self.timestampLabel.textColor = theme.textSecondaryColor
    }
}
