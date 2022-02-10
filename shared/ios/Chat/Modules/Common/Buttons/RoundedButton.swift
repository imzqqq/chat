import UIKit

final class RoundedButton: CustomRoundedButton, Themable {
    
    // MARK: - Constants
    
    private enum Constants {
        static let backgroundColorAlpha: CGFloat = 0.2
    }
    
    // MARK: - Properties
    
    // MARK: Private
    
    private var theme: Theme?
    
    // MARK: Public
    
    var actionStyle: UIAlertAction.Style = .default {
        didSet {
            self.updateButtonStyle()
        }
    }
    
    // MARK: - Life cycle
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.update(theme: ThemeService.shared().theme)
    }
    
    // MARK: - Private
    
    private func updateButtonStyle() {
        guard let theme = theme else {
            return
        }
        
        let backgroundColor: UIColor
        
        switch self.actionStyle {
        case .default:
            backgroundColor = theme.tintColor
        default:
            backgroundColor = theme.noticeColor
        }
        
        self.vc_setBackgroundColor(backgroundColor.withAlphaComponent(Constants.backgroundColorAlpha), for: .normal)
        self.setTitleColor(backgroundColor, for: .normal)
    }
    
    // MARK: - Themable
    
    func update(theme: Theme) {
        self.theme = theme
        self.updateButtonStyle()
    }
}
