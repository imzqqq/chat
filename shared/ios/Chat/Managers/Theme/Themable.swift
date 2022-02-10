import Foundation

@objc protocol Themable: AnyObject {
    func update(theme: Theme)
}
