import UIKit
import Reusable

final class BubbleReactionActionViewCell: UICollectionViewCell, NibReusable, Themable {
    
    // MARK: - Constants

    // MARK: - Properties
    
    // MARK: Outlets

    @IBOutlet private weak var actionLabel: UILabel!
    
    // MARK: Private
    
    private var theme: Theme?
    
    // MARK: Public
    
    // MARK: - Life cycle
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        /*
         On iOS 12, there are issues with self-sizing cells as described in Apple release notes (https://developer.apple.com/documentation/ios_release_notes/ios_12_release_notes) :
         "You might encounter issues with systemLayoutSizeFitting(_:) when using a UICollectionViewCell subclass that requires updateConstraints().
         (42138227) — Workaround: Don't call the cell's setNeedsUpdateConstraints() method unless you need to support live constraint changes.
         If you need to support live constraint changes, call updateConstraintsIfNeeded() before calling systemLayoutSizeFitting(_:)."
         */
        self.updateConstraintsIfNeeded()
        
        return super.preferredLayoutAttributesFitting(layoutAttributes)
    }
    
    // MARK: - Public
    
    func fill(actionString: String) {
        self.actionLabel.text = actionString
        self.updateViews()
    }
    
    func update(theme: Theme) {
        self.theme = theme
        self.updateViews()
    }
    
    // MARK: - Private
    
    private func updateViews() {
        self.actionLabel.textColor = self.theme?.tintColor
    }
}
