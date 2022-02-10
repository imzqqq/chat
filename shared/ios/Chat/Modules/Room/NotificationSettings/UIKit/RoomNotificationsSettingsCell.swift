import UIKit
import Reusable

class RoomNotificationSettingsCell: UITableViewCell {
    
    func update(state: RoomNotificationSettingsCellViewData) {
        textLabel?.text = state.notificicationState.title
        if state.selected {
            accessoryView = UIImageView(image: Asset.Images.checkmark.image)
        } else {
            accessoryView = nil
        }
    }
}

extension RoomNotificationSettingsCell: Reusable {}

extension RoomNotificationSettingsCell: Themable {
    func update(theme: Theme) {
        textLabel?.font = theme.fonts.body
        textLabel?.textColor = theme.textPrimaryColor
        backgroundColor = theme.backgroundColor
        contentView.backgroundColor = .clear
        tintColor = theme.tintColor
        selectedBackgroundView = UIView()
        selectedBackgroundView?.backgroundColor = theme.selectedBackgroundColor
    }
}
