import Foundation
import Reusable

extension MXKTableViewCellWithTextView: Reusable {}

extension MXKTableViewCellWithTextView: Themable {
    
    func update(theme: Theme) {
        mxkTextView.backgroundColor = .clear
        mxkTextView.textColor = theme.textPrimaryColor
        mxkTextView.tintColor = theme.tintColor
        backgroundColor = theme.backgroundColor
        contentView.backgroundColor = .clear
    }
    
}
