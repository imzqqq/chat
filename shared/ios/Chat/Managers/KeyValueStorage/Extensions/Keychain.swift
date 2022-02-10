import Foundation
import KeychainAccess

/// Extension on Keychain to get/set booleans
extension Keychain {
    
    public func set(_ value: Bool, key: String, ignoringAttributeSynchronizable: Bool = true) throws {
        try set(value.description, key: key, ignoringAttributeSynchronizable: ignoringAttributeSynchronizable)
    }
    
    public func getBool(_ key: String, ignoringAttributeSynchronizable: Bool = true) throws -> Bool? {
        guard let value = try getString(key, ignoringAttributeSynchronizable: ignoringAttributeSynchronizable) else {
            return nil
        }
        guard value == true.description || value == false.description else { return nil }
        return value == true.description
    }
    
}
