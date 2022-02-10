// File created from ScreenTemplate
// $ createScreen.sh ReactionHistory ReactionHistory

import Foundation

protocol ReactionHistoryCoordinatorDelegate: AnyObject {
    func reactionHistoryCoordinatorDidClose(_ coordinator: ReactionHistoryCoordinatorType)
}

/// `ReactionHistoryCoordinatorType` is a protocol describing a Coordinator that handle reaction history navigation flow.
protocol ReactionHistoryCoordinatorType: Coordinator, Presentable {
    var delegate: ReactionHistoryCoordinatorDelegate? { get }
}
