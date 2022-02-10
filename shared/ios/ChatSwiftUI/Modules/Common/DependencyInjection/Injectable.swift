import Foundation

/// A protocol for classes that can be injected with a dependency container
protocol Injectable: AnyObject {
    var dependencies: DependencyContainer! { get set }
}


extension Injectable {
    
    /// Used to inject the dependency container into an Injectable.
    /// - Parameter dependencies: The `DependencyContainer` to inject.
    func inject(dependencies: DependencyContainer) {
        self.dependencies = dependencies
    }
}
