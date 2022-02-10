import Foundation

protocol FlowTemplateCoordinatorDelegate: AnyObject {
    func flowTemplateCoordinatorDidComplete(_ coordinator: FlowTemplateCoordinatorProtocol)
    
    /// Called when the view has been dismissed by gesture when presented modally (not in full screen).
    func flowTemplateCoordinatorDidDismissInteractively(_ coordinator: FlowTemplateCoordinatorProtocol)
}

/// `FlowTemplateCoordinatorProtocol` is a protocol describing a Coordinator that handle xxxxxxx navigation flow.
protocol FlowTemplateCoordinatorProtocol: Coordinator, Presentable {
    var delegate: FlowTemplateCoordinatorDelegate? { get }
}
