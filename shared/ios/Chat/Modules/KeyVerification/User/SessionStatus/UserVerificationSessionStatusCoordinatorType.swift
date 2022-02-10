// File created from ScreenTemplate
// $ createScreen.sh SessionStatus UserVerificationSessionStatus

import Foundation

protocol UserVerificationSessionStatusCoordinatorDelegate: AnyObject {
    func userVerificationSessionStatusCoordinator(_ coordinator: UserVerificationSessionStatusCoordinatorType, wantsToVerifyDeviceWithId deviceId: String, for userId: String)
    func userVerificationSessionStatusCoordinator(_ coordinator: UserVerificationSessionStatusCoordinatorType, wantsToManuallyVerifyDeviceWithId deviceId: String, for userId: String)
    func userVerificationSessionStatusCoordinatorDidClose(_ coordinator: UserVerificationSessionStatusCoordinatorType)
}

/// `UserVerificationSessionStatusCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol UserVerificationSessionStatusCoordinatorType: Coordinator, Presentable {
    var delegate: UserVerificationSessionStatusCoordinatorDelegate? { get }
}
