import UIKit
import Reusable

protocol ServiceTermsModalTableHeaderViewDelegate: AnyObject {
    func tableHeaderViewDidTapInformationButton()
}

class ServiceTermsModalTableHeaderView: UIView, NibLoadable, Themable {
    
    // MARK: - Properties
    
    weak var delegate: ServiceTermsModalTableHeaderViewDelegate?
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var serviceURLLabel: UILabel!
    
    // MARK: - Setup
    
    static func instantiate() -> Self {
        let view = Self.loadFromNib()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.update(theme: ThemeService.shared().theme)
        return view
    }
    
    func update(theme: Theme) {
        titleLabel.font = theme.fonts.footnote
        titleLabel.textColor = theme.colors.secondaryContent
        
        serviceURLLabel.font = theme.fonts.callout
        serviceURLLabel.textColor = theme.colors.secondaryContent
    }
    
    // MARK: - Action
    
    @IBAction private func buttonAction(_ sender: Any) {
        delegate?.tableHeaderViewDidTapInformationButton()
    }
    
}
