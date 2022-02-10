import Foundation

/// `BubbleCellReadReceiptsDisplayable` is a protocol indicating that a cell support displaying read receipts.
@objc protocol BubbleCellReadReceiptsDisplayable {
    func addReadReceiptsView(_ readReceiptsView: UIView)
    func removeReadReceiptsView()
}
