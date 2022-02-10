import UIKit

/// Dialpad action button type
@objc enum DialpadActionButtonType: Int {
    case backspace
    case call
}

/// Action button class for Dialpad screen
class DialpadActionButton: DialpadButton {

    var type: DialpadActionButtonType = .backspace
    
    override func update(theme: Theme) {
        switch type {
        case .backspace:
            backgroundColor = .clear
            tintColor = theme.colors.tertiaryContent
        case .call:
            backgroundColor = theme.colors.accent
            tintColor = .white
        }
    }

}
