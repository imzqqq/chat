import Foundation

@objcMembers
final class RoomContextualMenuItem: NSObject {
    
    // MARK: - Properties
    
    let title: String
    let image: UIImage?
    
    var isEnabled: Bool = true
    var action: (() -> Void)?
    
    // MARK: - Setup
    
    init(menuAction: RoomContextualMenuAction) {
        self.title = menuAction.title
        self.image = menuAction.image
        super.init()
    }
}
