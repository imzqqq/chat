import Foundation

/// `BubbleCellReactionsDisplayable` is a protocol indicating that a cell support displaying reactions.
@objc protocol BubbleCellReactionsDisplayable {
    func addReactionsView(_ reactionsView: UIView)
    func removeReactionsView()
}
