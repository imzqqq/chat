import Foundation

/// FlowTemplateCoordinator input parameters
struct FlowTemplateCoordinatorParameters {
    
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
