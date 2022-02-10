import UIKit
import Reusable

/// Table view cell with only a text field spanning the whole content view, insets can be configured via `textField.insets`
class TextFieldTableViewCell: UITableViewCell {

    @IBOutlet weak var textField: InsettedTextField!
    
}

extension TextFieldTableViewCell: NibReusable {}

extension TextFieldTableViewCell: Themable {
    
    func update(theme: Theme) {
        theme.applyStyle(onTextField: textField)
        textField.placeholderColor = theme.placeholderTextColor
    }
    
}
