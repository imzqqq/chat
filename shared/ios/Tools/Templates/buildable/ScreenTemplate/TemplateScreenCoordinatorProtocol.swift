import Foundation

protocol TemplateScreenCoordinatorDelegate: AnyObject {
    func templateScreenCoordinator(_ coordinator: TemplateScreenCoordinatorProtocol, didCompleteWithUserDisplayName userDisplayName: String?)
    func templateScreenCoordinatorDidCancel(_ coordinator: TemplateScreenCoordinatorProtocol)
}

/// `TemplateScreenCoordinatorProtocol` is a protocol describing a Coordinator that handle xxxxxxx navigation flow.
protocol TemplateScreenCoordinatorProtocol: Coordinator, Presentable {
    var delegate: TemplateScreenCoordinatorDelegate? { get }
}
