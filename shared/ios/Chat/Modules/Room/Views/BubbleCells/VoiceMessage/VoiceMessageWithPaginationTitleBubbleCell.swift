import Foundation

class VoiceMessageWithPaginationTitleBubbleCell: VoiceMessageBubbleCell {
    override func setupViews() {
        super.setupViews()
        
        bubbleCellContentView?.showPaginationTitle = true
    }
}
