import UIKit

/// AutosizedCollectionView is a convenient UICollectionView that makes dynamic sizing easier when using Auto Layout
class AutosizedCollectionView: UICollectionView {
    
    override var contentSize: CGSize {
        didSet {
            self.invalidateIntrinsicContentSize()
        }
    }
    
    override var intrinsicContentSize: CGSize {
        return self.contentSize
    }
}
