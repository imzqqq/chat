import UIKit
import Reusable

/// Table view cell with only a text view spanning the whole content view, insets can be configured via `textView.textContainerInset`
class TextViewTableViewCell: UITableViewCell {

    @IBOutlet weak var textView: PlaceholderedTextView!
    
}

extension TextViewTableViewCell: NibReusable {}

extension TextViewTableViewCell: Themable {
    
    func update(theme: Theme) {
        textView.textColor = theme.textPrimaryColor
        textView.tintColor = theme.tintColor
        textView.placeholderColor = theme.placeholderTextColor
    }
    
}
