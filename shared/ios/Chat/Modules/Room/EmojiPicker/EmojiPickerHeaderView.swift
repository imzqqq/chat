import UIKit
import Reusable

final class EmojiPickerHeaderView: UICollectionReusableView, NibReusable {
    
    // MARK: - Properties
    
    @IBOutlet private weak var titleLabel: UILabel!
    
    // MARK: - Public
    
    func update(theme: Theme) {
        self.backgroundColor = theme.headerBackgroundColor
        self.titleLabel.textColor = theme.headerTextPrimaryColor
    }
    
    func fill(with title: String) {
        titleLabel.text = title
    }
}
