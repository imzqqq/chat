import Foundation

@objc
public protocol RecentsListServiceDelegate: AnyObject {
    
    /// Delegate method to be called when service data updated
    /// - Parameter service: service object
    func serviceDidChangeData(_ service: RecentsListServiceProtocol)
}
