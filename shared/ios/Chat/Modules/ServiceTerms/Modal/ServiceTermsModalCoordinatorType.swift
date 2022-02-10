// File created from FlowTemplate
// $ createRootCoordinator.sh Modal ServiceTermsModal ServiceTermsModalLoadTermsScreen

import Foundation

protocol ServiceTermsModalCoordinatorDelegate: AnyObject {
    func serviceTermsModalCoordinatorDidAccept(_ coordinator: ServiceTermsModalCoordinatorType)
    func serviceTermsModalCoordinatorDidDecline(_ coordinator: ServiceTermsModalCoordinatorType)
    func serviceTermsModalCoordinatorDidDismissInteractively(_ coordinator: ServiceTermsModalCoordinatorType)
}

/// `ServiceTermsModalCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol ServiceTermsModalCoordinatorType: Coordinator, Presentable {
    var delegate: ServiceTermsModalCoordinatorDelegate? { get }
}
