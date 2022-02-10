import Foundation
import Reusable

class RoomNotificationSettingsAvatarView: UIView {
    
    @IBOutlet weak var avatarView: RoomAvatarView!
    @IBOutlet weak var nameLabel: UILabel!
    
    func configure(viewData: AvatarViewDataProtocol) {
        avatarView.fill(with: viewData)
        
        switch viewData.fallbackImage {
        case .matrixItem(_, let matrixItemDisplayName):
            nameLabel.text = matrixItemDisplayName
        default:
            nameLabel.text = nil
        }
    }
}

extension RoomNotificationSettingsAvatarView: NibLoadable { }
extension RoomNotificationSettingsAvatarView: Themable {
    func update(theme: Theme) {
        nameLabel?.font = theme.fonts.title3SB
        nameLabel?.textColor = theme.textPrimaryColor
    }
}
