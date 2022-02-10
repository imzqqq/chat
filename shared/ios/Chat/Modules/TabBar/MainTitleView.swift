import Foundation

@objcMembers
class MainTitleView: UIStackView, Themable {
    
    // MARK: - Properties
    
    public private(set) var titleLabel: UILabel!
    public private(set) var subtitleLabel: UILabel!
    
    // MARK: - Lifecycle
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    required init(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    // MARK: - Themable
    
    func update(theme: Theme) {
        self.titleLabel.textColor = theme.colors.primaryContent
        self.titleLabel.font = theme.fonts.calloutSB
        
        self.subtitleLabel.textColor = theme.colors.tertiaryContent
        self.subtitleLabel.font = theme.fonts.footnote
    }
    
    // MARK: - Private
    
    private func setupView() {
        self.titleLabel = UILabel(frame: .zero)
        self.titleLabel.backgroundColor = .clear

        self.subtitleLabel = UILabel(frame: .zero)
        self.subtitleLabel.backgroundColor = .clear

        self.addArrangedSubview(titleLabel)
        self.addArrangedSubview(subtitleLabel)
        self.distribution = .equalCentering
        self.axis = .vertical
        self.alignment = .center
        self.spacing = 0.5
    }
}
