import Foundation

@available(iOS 14, *)
protocol UserSuggestionViewModelProtocol {
    
    static func makeUserSuggestionViewModel(userSuggestionService: UserSuggestionServiceProtocol) -> UserSuggestionViewModelProtocol
    
    var context: UserSuggestionViewModelType.Context { get }
    
    var completion: ((UserSuggestionViewModelResult) -> Void)? { get set }
}
