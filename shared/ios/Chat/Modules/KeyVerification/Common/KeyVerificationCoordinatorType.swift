// File created from FlowTemplate
// $ createRootCoordinator.sh DeviceVerification DeviceVerification DeviceVerificationStart

import Foundation

protocol KeyVerificationCoordinatorDelegate: AnyObject {
    func keyVerificationCoordinatorDidComplete(_ coordinator: KeyVerificationCoordinatorType, otherUserId: String, otherDeviceId: String)
    func keyVerificationCoordinatorDidCancel(_ coordinator: KeyVerificationCoordinatorType)
}

/// `KeyVerificationCoordinatorType` is a protocol describing a Coordinator that handle key verification navigation flow.
protocol KeyVerificationCoordinatorType: Coordinator, Presentable {
    var delegate: KeyVerificationCoordinatorDelegate? { get }
}
