import Foundation
import Reusable

/// `RootTabEmptyView` is a view to display when there is no UI item to display on a screen.
@objcMembers
final class RootTabEmptyView: UIView, NibLoadable {
    
    // MARK: - Properties
    
    // MARK: Outlets
    
    @IBOutlet private weak var imageView: UIImageView!
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var informationLabel: UILabel!
    @IBOutlet private(set) weak var contentView: UIView!
    
    // MARK: Private
    
    private var theme: Theme!
    
    // MARK: Public
    
    // MARK: - Setup
    
    class func instantiate() -> RootTabEmptyView {
        let view = RootTabEmptyView.loadFromNib()
        view.theme = ThemeService.shared().theme
        return view
    }
    
    // MARK: - Life cycle
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.informationLabel.text = VectorL10n.homeEmptyViewInformation
    }
    
    // MARK: - Public
    
    func fill(with image: UIImage, title: String, informationText: String) {
        self.imageView.image = image
        self.titleLabel.text = title
        self.informationLabel.text = informationText
    }
}

// MARK: - Themable
extension RootTabEmptyView: Themable {
    
    func update(theme: Theme) {
        self.theme = theme
        
        self.backgroundColor = theme.backgroundColor
        
        self.titleLabel.textColor = theme.textPrimaryColor
        self.informationLabel.textColor = theme.textSecondaryColor
    }
}
