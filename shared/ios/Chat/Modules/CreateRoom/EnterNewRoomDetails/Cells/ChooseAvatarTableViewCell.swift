import UIKit
import Reusable

protocol ChooseAvatarTableViewCellDelegate: AnyObject {
    func chooseAvatarTableViewCellDidTapChooseAvatar(_ cell: ChooseAvatarTableViewCell, sourceView: UIView)
}

class ChooseAvatarTableViewCell: UITableViewCell {

    @IBOutlet private weak var avatarImageView: UIImageView! {
        didSet {
            avatarImageView.layer.cornerRadius = avatarImageView.frame.width/2
        }
    }
    @IBOutlet private weak var chooseAvatarButton: UIButton!
    
    weak var delegate: ChooseAvatarTableViewCellDelegate?
    
    @IBAction private func chooseAvatarButtonTapped(_ sender: UIButton) {
        delegate?.chooseAvatarTableViewCellDidTapChooseAvatar(self, sourceView: sender)
    }
    
    func configure(withViewModel viewModel: ChooseAvatarTableViewCellVM) {
        avatarImageView.image = viewModel.avatarImage
    }
    
}

extension ChooseAvatarTableViewCell: NibReusable {}

extension ChooseAvatarTableViewCell: Themable {
    
    func update(theme: Theme) {
        backgroundView = UIView()
        backgroundView?.backgroundColor = theme.backgroundColor
        avatarImageView.backgroundColor = theme.tintColor
    }
    
}
