import Foundation

protocol TabBarCoordinatorDelegate: AnyObject {
    // TODO: Remove this method, authentication should not be handled by TabBarCoordinator
    func tabBarCoordinatorDidCompleteAuthentication(_ coordinator: TabBarCoordinatorType)
}

/// `TabBarCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol TabBarCoordinatorType: Coordinator, SplitViewMasterPresentable {
    
    var delegate: TabBarCoordinatorDelegate? { get }
        
    /// Start coordinator by selecting a Space.
    /// - Parameter spaceId: The id of the Space to use.
    func start(with spaceId: String?)
    
    func popToHome(animated: Bool, completion: (() -> Void)?)
    
    // TODO: Remove this method, this implementation detail should not be exposed
    // Release the current selected item (room/contact/group...).
    func releaseSelectedItems()
}
