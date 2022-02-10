import Foundation

/// MXKRoomBubbleTableViewCell layout constants
@objcMembers
final class RoomBubbleCellLayout: NSObject {
    
    // Reactions
    
    static let reactionsViewTopMargin: CGFloat = 1.0
    static let reactionsViewLeftMargin: CGFloat = 55.0
    static let reactionsViewRightMargin: CGFloat = 15.0
    
    // Read receipts
    
    static let readReceiptsViewTopMargin: CGFloat = 5.0
    static let readReceiptsViewRightMargin: CGFloat = 6.0
    static let readReceiptsViewHeight: CGFloat = 16.0
    static let readReceiptsViewWidth: CGFloat = 150.0
    
    // Read marker
    
    static let readMarkerViewHeight: CGFloat = 2.0
    
    // Timestamp
    
    static let timestampLabelHeight: CGFloat = 18.0
    static let timestampLabelWidth: CGFloat = 39.0
    
    // Others
    
    static let encryptedContentLeftMargin: CGFloat = 15.0
    static let urlPreviewViewTopMargin: CGFloat = 8.0
}
