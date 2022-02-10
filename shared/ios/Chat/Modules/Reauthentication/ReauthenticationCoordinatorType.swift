// File created from FlowTemplate
// $ createRootCoordinator.sh Reauthentication Reauthentication

import Foundation

protocol ReauthenticationCoordinatorDelegate: AnyObject {
    func reauthenticationCoordinatorDidComplete(_ coordinator: ReauthenticationCoordinatorType, withAuthenticationParameters: [String: Any]?)
    func reauthenticationCoordinatorDidCancel(_ coordinator: ReauthenticationCoordinatorType)
    func reauthenticationCoordinator(_ coordinator: ReauthenticationCoordinatorType, didFailWithError: Error)
}

/// `ReauthenticationCoordinatorType` is a protocol describing a Coordinator that handle reauthentication. It is used before calling an authenticated API.
protocol ReauthenticationCoordinatorType: Coordinator, Presentable {
    var delegate: ReauthenticationCoordinatorDelegate? { get }
}
