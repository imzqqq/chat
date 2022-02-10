import Foundation

/// Class that can be extended that supports injection and the `@Inject` property wrapper.
open class InjectableObject: Injectable {
    var dependencies: DependencyContainer!
}
