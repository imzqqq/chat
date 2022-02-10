import Foundation
import Combine

@available(iOS 14.0, *)
protocol UserSuggestionItemProtocol: Avatarable {
    var userId: String { get }
    var displayName: String? { get }
    var avatarUrl: String? { get }
}

@available(iOS 14.0, *)
protocol UserSuggestionServiceProtocol {
    
    var items: CurrentValueSubject<[UserSuggestionItemProtocol], Never> { get }
    
    var currentTextTrigger: String? { get }
    
    func processTextMessage(_ textMessage: String?)
}

// MARK: Avatarable

@available(iOS 14.0, *)
extension UserSuggestionItemProtocol {
    var mxContentUri: String? {
        avatarUrl
    }
    var matrixItemId: String {
        userId
    }
}
