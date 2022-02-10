import Foundation

protocol SplitViewCoordinatorDelegate: AnyObject {
    // TODO: Remove this method, authentication should not be handled by SplitViewCoordinator
    func splitViewCoordinatorDidCompleteAuthentication(_ coordinator: SplitViewCoordinatorType)
}

/// `SplitViewCoordinatorType` is a protocol describing a Coordinator that handles split view navigation flow.
protocol SplitViewCoordinatorType: Coordinator, Presentable {
    
    var delegate: SplitViewCoordinatorDelegate? { get }
    
    /// Start coordinator by selecting a Space.
    /// - Parameter spaceId: The id of the Space to use.
    func start(with spaceId: String?)
    
    /// Restore navigation stack and show home screen
    func popToHome(animated: Bool, completion: (() -> Void)?)
        
    // TODO: Do not expose publicly this method
    /// Remove detail screens and display placeholder if needed 
    func resetDetails(animated: Bool)
}
