import Foundation

/// A property wrapped used to inject from the dependency container on the instance, to instance properties.
///
/// ```
/// @Inject var someClass: SomeClass
/// ```
@propertyWrapper struct Inject<Value> {
    
    static subscript<T: Injectable>(
        _enclosingInstance instance: T,
        wrapped wrappedKeyPath: ReferenceWritableKeyPath<T, Value>,
        storage storageKeyPath: ReferenceWritableKeyPath<T, Self>
    ) -> Value {
        get {
            // Resolve dependencies from enclosing instance's `dependencies` property
            let v: Value = instance.dependencies.resolve()
            return v
        }
        set {
            fatalError("Only subscript get is supported for injection")
        }
    }
    
    @available(*, unavailable, message:  "This property wrapper can only be applied to classes")
    var wrappedValue: Value {
        get { fatalError("wrappedValue get not used") }
        set { fatalError("wrappedValue set not used. \(newValue)" ) }
    }
}
