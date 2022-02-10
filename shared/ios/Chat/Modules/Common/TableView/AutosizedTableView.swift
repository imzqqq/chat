import UIKit

/// AutosizedTableView is a convenient UITableView that makes dynamic sizing easier when using Auto Layout
class AutosizedTableView: UITableView {
    
    override var contentSize: CGSize {
        didSet {
            self.invalidateIntrinsicContentSize()
        }
    }
    
    override var intrinsicContentSize: CGSize {
        return self.contentSize
    }
    
}
