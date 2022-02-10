import Foundation

@objc enum RoomContextualMenuAction: Int {
    case copy
    case reply
    case edit
    case more
    case resend
    case delete
    
    // MARK: - Properties
    
    var title: String {
        let title: String
        
        switch self {
        case .copy:
            title = VectorL10n.roomEventActionCopy
        case .reply:
            title = VectorL10n.roomEventActionReply
        case .edit:
            title = VectorL10n.roomEventActionEdit
        case .more:
            title = VectorL10n.roomEventActionMore
        case .resend:
            title = VectorL10n.retry
        case .delete:
            title = VectorL10n.roomEventActionDelete
        }
        
        return title
    }
    
    var image: UIImage? {
        let image: UIImage?
        
        switch self {
        case .copy:
            image = Asset.Images.roomContextMenuCopy.image
        case .reply:
            image = Asset.Images.roomContextMenuReply.image
        case .edit:
            image = Asset.Images.roomContextMenuEdit.image
        case .more:
            image = Asset.Images.roomContextMenuMore.image
        case .resend:
            image = Asset.Images.roomContextMenuRetry.image
        case .delete:
            image = Asset.Images.roomContextMenuDelete.image
        default:
            image = nil
        }
        
        return image
    }
}
