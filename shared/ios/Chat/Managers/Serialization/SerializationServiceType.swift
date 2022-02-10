import Foundation

protocol SerializationServiceType {
    func deserialize<T: Decodable>(_ data: Data) throws -> T
    func deserialize<T: Decodable>(_ object: Any) throws -> T
    
    func serialize<T: Encodable>(_ object: T) throws -> Data
}
