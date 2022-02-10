// File created from FlowTemplate
// $ createRootCoordinator.sh UserVerification UserVerification

import Foundation

protocol UserVerificationCoordinatorDelegate: AnyObject {
    func userVerificationCoordinatorDidComplete(_ coordinator: UserVerificationCoordinatorType)
}

/// `UserVerificationCoordinatorType` is a protocol describing a Coordinator that handle user verification navigation flow.
protocol UserVerificationCoordinatorType: Coordinator, Presentable {
    var delegate: UserVerificationCoordinatorDelegate? { get }
}
