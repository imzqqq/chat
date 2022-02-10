import Foundation
import Reusable

class TitleHeaderView: UITableViewHeaderFooterView {
    
    @IBOutlet weak var label: UILabel!
    
    func update(title: String) {
        label.text = title.uppercased()
    }
    
}


extension TitleHeaderView: NibReusable {}
extension TitleHeaderView: Themable {
    
    func update(theme: Theme) {
        contentView.backgroundColor = theme.headerBackgroundColor
        label.textColor = theme.headerTextSecondaryColor
        label.font = theme.fonts.body
    }
}
