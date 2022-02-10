// File created from ScreenTemplate
// $ createScreen.sh Modal/Show ServiceTermsModalScreen

import Foundation

protocol ServiceTermsModalScreenCoordinatorDelegate: AnyObject {
    func serviceTermsModalScreenCoordinatorDidAccept(_ coordinator: ServiceTermsModalScreenCoordinatorType)
    func serviceTermsModalScreenCoordinator(_ coordinator: ServiceTermsModalScreenCoordinatorType, displayPolicy policy: MXLoginPolicyData)
    func serviceTermsModalScreenCoordinatorDidDecline(_ coordinator: ServiceTermsModalScreenCoordinatorType)
}

/// `ServiceTermsModalScreenCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol ServiceTermsModalScreenCoordinatorType: Coordinator, Presentable {
    var delegate: ServiceTermsModalScreenCoordinatorDelegate? { get }
}
