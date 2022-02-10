import UIKit

class InsettedTextField: UITextField {
    
    var insets: UIEdgeInsets = .zero {
        didSet {
            setNeedsLayout()
        }
    }
    
    var placeholderColor: UIColor? {
        didSet {
            updateAttributedPlaceholder()
        }
    }
    
    override var placeholder: String? {
        didSet {
            updateAttributedPlaceholder()
        }
    }
    
    private func updateAttributedPlaceholder() {
        guard let placeholder = placeholder else { return }
        guard let color = placeholderColor else { return }
        attributedPlaceholder = NSAttributedString(string: placeholder, attributes: [
            NSAttributedString.Key.foregroundColor: color
        ])
    }
    
    override func placeholderRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: insets)
    }
    
    override func textRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: insets)
    }
    
    override func editingRect(forBounds bounds: CGRect) -> CGRect {
        return bounds.inset(by: insets)
    }
    
}
