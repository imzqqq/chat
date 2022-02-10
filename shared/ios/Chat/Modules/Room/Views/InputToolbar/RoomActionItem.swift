import UIKit

@objcMembers
class RoomActionItem: NSObject {
    let image: UIImage
    let action: (() -> Void)

    init(image: UIImage, andAction action: @escaping () -> Void) {
        self.image = image
        self.action = action
        
        super.init()
    }
}
