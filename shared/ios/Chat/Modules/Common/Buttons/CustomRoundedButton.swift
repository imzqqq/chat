import UIKit

class CustomRoundedButton: UIButton {
    
    // MARK: - Constants
    
    private enum Constants {
        static let cornerRadius: CGFloat = 6.0
        static let fontSize: CGFloat = 17.0
    }
    
    // MARK: Setup
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.commonInit()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        self.commonInit()
    }
    
    private func commonInit() {
        self.layer.masksToBounds = true
        self.titleLabel?.font = UIFont.systemFont(ofSize: Constants.fontSize)        
        self.layer.cornerRadius = Constants.cornerRadius
    }
}
