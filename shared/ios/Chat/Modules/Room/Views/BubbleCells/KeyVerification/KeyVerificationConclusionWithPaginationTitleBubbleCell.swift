import UIKit

@objcMembers
final class KeyVerificationConclusionWithPaginationTitleBubbleCell: KeyVerificationConclusionBubbleCell {
    
    // MARK: - Constants
    
    private enum Sizing {
        static let view = KeyVerificationConclusionWithPaginationTitleBubbleCell(style: .default, reuseIdentifier: nil)
    }
    
    // MARK: - Setup
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        self.commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func commonInit() {
        guard let bubbleCellContentView = self.bubbleCellContentView else {
            fatalError("[KeyVerificationConclusionWithPaginationTitleBubbleCell] bubbleCellContentView should not be nil")
        }
        
        bubbleCellContentView.showPaginationTitle = true
    }
    
    // MARK: - Overrides
    
    override class func sizingView() -> KeyVerificationBaseBubbleCell {
        return self.Sizing.view
    }
}
