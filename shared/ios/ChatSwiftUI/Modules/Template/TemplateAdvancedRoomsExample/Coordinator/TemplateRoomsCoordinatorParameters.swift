import Foundation

/// TemplateRoomsCoordinator input parameters
struct TemplateRoomsCoordinatorParameters {
    
    /// The Matrix session
    let session: MXSession
                
    /// The navigation router that manage physical navigation
    let navigationRouter: NavigationRouterType
    
    init(session: MXSession,
         navigationRouter: NavigationRouterType? = nil) {
        self.session = session
        self.navigationRouter = navigationRouter ?? NavigationRouter(navigationController: ChatNavigationController())
    }
}
