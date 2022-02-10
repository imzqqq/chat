import Foundation

/// `NavigationRouterStoreProtocol` describes a structure that enables to get a NavigationRouter from a UINavigationController instance.
protocol NavigationRouterStoreProtocol {
    
    /// Gets the existing navigation router for the supplied controller, creating a new one if it doesn't yet exist.
    /// Note: The store only holds a weak reference to the returned router. It is the caller's responsibility to retain it.
    func navigationRouter(for navigationController: UINavigationController) -> NavigationRouterType
}
