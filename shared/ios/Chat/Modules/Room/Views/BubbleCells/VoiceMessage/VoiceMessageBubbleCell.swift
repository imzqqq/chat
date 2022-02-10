import Foundation

class VoiceMessageBubbleCell: SizableBaseBubbleCell, BubbleCellReactionsDisplayable {
    
    private var playbackController: VoiceMessagePlaybackController!
    
    override func render(_ cellData: MXKCellData!) {
        super.render(cellData)
        
        guard let data = cellData as? RoomBubbleCellData else {
            return
        }
        
        guard data.attachment.type == MXKAttachmentTypeVoiceMessage else {
            fatalError("Invalid attachment type passed to a voice message cell.")
        }
        
        if playbackController.attachment != data.attachment {
            playbackController.attachment = data.attachment
        }
    }
    
    override func setupViews() {
        super.setupViews()
        
        bubbleCellContentView?.backgroundColor = .clear
        bubbleCellContentView?.showSenderInfo = true
        bubbleCellContentView?.showPaginationTitle = false
        
        guard let contentView = bubbleCellContentView?.innerContentView else {
            return
        }
        
        playbackController = VoiceMessagePlaybackController(mediaServiceProvider: VoiceMessageMediaServiceProvider.sharedProvider,
                                                            cacheManager: VoiceMessageAttachmentCacheManager.sharedManager)
        
        contentView.vc_addSubViewMatchingParent(playbackController.playbackView)
    }
}
