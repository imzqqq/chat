import UIKit
import Reusable

class VerifyEmojiCollectionViewCell: UICollectionViewCell, Reusable, Themable {
    @IBOutlet weak var emoji: UILabel!
    @IBOutlet weak var name: UILabel!

    func update(theme: Theme) {
        name.textColor = theme.textPrimaryColor
    }
}
