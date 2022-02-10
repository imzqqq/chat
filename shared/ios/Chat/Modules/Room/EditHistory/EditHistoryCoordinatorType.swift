// File created from ScreenTemplate
// $ createScreen.sh Room/EditHistory EditHistory

import Foundation

protocol EditHistoryCoordinatorDelegate: AnyObject {
    func editHistoryCoordinatorDidComplete(_ coordinator: EditHistoryCoordinatorType)
}

/// `EditHistoryCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol EditHistoryCoordinatorType: Coordinator, Presentable {
    var delegate: EditHistoryCoordinatorDelegate? { get }
}
