import UIKit
import Reusable

class HorizontalButtonsContainerView: UIView {
    
    private enum Constants {
        static let stackViewTopMargin: CGFloat = 8
        static let stackViewBottomMargin: CGFloat = 16
    }

    @IBOutlet weak private var stackView: UIStackView!
    
    @IBOutlet weak var firstButton: CallTileActionButton!
    @IBOutlet weak var secondButton: CallTileActionButton!
    
    override var intrinsicContentSize: CGSize {
        var result = stackView.intrinsicContentSize
        result.width = self.frame.width
        result.height += Constants.stackViewTopMargin + Constants.stackViewBottomMargin
        return result
    }

}

extension HorizontalButtonsContainerView: NibLoadable {}

extension HorizontalButtonsContainerView: Themable {
    
    func update(theme: Theme) {
        firstButton.update(theme: theme)
        secondButton.update(theme: theme)
    }
    
}
