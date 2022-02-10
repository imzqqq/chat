import Foundation

class VoiceMessageWithoutSenderInfoBubbleCell: VoiceMessageBubbleCell {
    override func setupViews() {
        super.setupViews()
        
        bubbleCellContentView?.showSenderInfo = false
    }
}
