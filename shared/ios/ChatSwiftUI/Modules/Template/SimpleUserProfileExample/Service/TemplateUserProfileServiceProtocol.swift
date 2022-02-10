import Foundation
import Combine

@available(iOS 14.0, *)
protocol TemplateUserProfileServiceProtocol: Avatarable {
    var userId: String { get }
    var displayName: String? { get }
    var avatarUrl: String? { get }
    var presenceSubject: CurrentValueSubject<TemplateUserProfilePresence, Never> { get }
}

// MARK: Avatarable

@available(iOS 14.0, *)
extension TemplateUserProfileServiceProtocol {
    var mxContentUri: String? {
        avatarUrl
    }
    var matrixItemId: String {
        userId
    }
}
