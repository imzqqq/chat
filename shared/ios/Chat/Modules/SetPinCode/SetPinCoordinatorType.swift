// File created from FlowTemplate
// $ createRootCoordinator.sh SetPinCode SetPin EnterPinCode

import Foundation

protocol SetPinCoordinatorDelegate: AnyObject {
    func setPinCoordinatorDidComplete(_ coordinator: SetPinCoordinatorType)
    func setPinCoordinatorDidCompleteWithReset(_ coordinator: SetPinCoordinatorType, dueToTooManyErrors: Bool)
    func setPinCoordinatorDidCancel(_ coordinator: SetPinCoordinatorType)
}

/// `SetPinCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol SetPinCoordinatorType: Coordinator, Presentable {
    var delegate: SetPinCoordinatorDelegate? { get }
}
