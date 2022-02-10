// File created from ScreenTemplate
// $ createScreen.sh Communities GroupDetails

import Foundation

protocol GroupDetailsCoordinatorDelegate: AnyObject {
    func groupDetailsCoordinatorDidCancel(_ coordinator: GroupDetailsCoordinatorProtocol)
}

/// `GroupDetailsCoordinatorProtocol` is a protocol describing a Coordinator that handle communities navigation flow.
protocol GroupDetailsCoordinatorProtocol: Coordinator, Presentable {
    var delegate: GroupDetailsCoordinatorDelegate? { get }
}
