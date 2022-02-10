// File created from FlowTemplate
// $ createRootCoordinator.sh CrossSigning CrossSigningSetup

import Foundation

protocol CrossSigningSetupCoordinatorDelegate: AnyObject {
    func crossSigningSetupCoordinatorDidComplete(_ coordinator: CrossSigningSetupCoordinatorType)
    func crossSigningSetupCoordinatorDidCancel(_ coordinator: CrossSigningSetupCoordinatorType)
    func crossSigningSetupCoordinator(_ coordinator: CrossSigningSetupCoordinatorType, didFailWithError error: Error)
}

/// `CrossSigningSetupCoordinatorType` is a protocol describing a Coordinator that handles cross signing setup navigation flow.
protocol CrossSigningSetupCoordinatorType: Coordinator, Presentable {
    var delegate: CrossSigningSetupCoordinatorDelegate? { get }
}
