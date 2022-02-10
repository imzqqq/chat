import Foundation
import Reusable

extension MXKTableViewCellWithLabelAndSwitch: Reusable {}

extension MXKTableViewCellWithLabelAndSwitch: Themable {

    func update(theme: Theme) {
        mxkLabel.textColor = theme.textPrimaryColor
        backgroundColor = theme.backgroundColor
        contentView.backgroundColor = .clear
        mxkSwitch.onTintColor = theme.tintColor
   }

}
