// File created from ScreenTemplate
// $ createScreen.sh UserVerification UserVerificationSessionsStatus

import Foundation

protocol UserVerificationSessionsStatusCoordinatorDelegate: AnyObject {
    func userVerificationSessionsStatusCoordinatorDidClose(_ coordinator: UserVerificationSessionsStatusCoordinatorType)
    func userVerificationSessionsStatusCoordinator(_ coordinator: UserVerificationSessionsStatusCoordinatorType, didSelectDeviceWithId deviceId: String, for userId: String)
}

/// `UserVerificationSessionsStatusCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol UserVerificationSessionsStatusCoordinatorType: Coordinator, Presentable {
    var delegate: UserVerificationSessionsStatusCoordinatorDelegate? { get }
}
