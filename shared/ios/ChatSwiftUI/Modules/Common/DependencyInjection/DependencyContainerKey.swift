import Foundation
import SwiftUI

/// An Environment Key for retrieving runtime dependencies.
///
/// Dependencies are to be injected into `ObservableObjects`
/// that are owned by a View (i.e. `@StateObject`'s, such as ViewModels owned by the View).
private struct DependencyContainerKey: EnvironmentKey {
    static let defaultValue = DependencyContainer()
}

@available(iOS 14.0, *)
extension EnvironmentValues {
    var dependencies: DependencyContainer {
        get { self[DependencyContainerKey.self] }
        set { self[DependencyContainerKey.self] = newValue }
    }
}

@available(iOS 14.0, *)
extension View {
    
    /// A modifier for adding a dependency to the SwiftUI view hierarchy's dependency container.
    ///
    /// Important: When adding a dependency to cast it to the type in which it will be injected.
    /// So if adding `MockDependency` but type at injection is `Dependency` remember to cast
    /// to `Dependency` first.
    /// - Parameter dependency: The dependency to add.
    /// - Returns: The wrapped view that now includes the dependency.
    func addDependency<T>(_ dependency: T) -> some View {
        transformEnvironment(\.dependencies) { container in
            container.register(dependency: dependency)
        }
    }
}
