import Foundation

protocol AvatarInputProtocol: AvatarProtocol {
    var mxContentUri: String? { get }
    var matrixItemId: String { get }
    var displayName: String? { get }
}

struct AvatarInput: AvatarInputProtocol {
    let mxContentUri: String?
    var matrixItemId: String
    let displayName: String?
}

extension AvatarInput: Equatable { }
