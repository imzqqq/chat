import Foundation
import Combine

@available(iOS 14.0, *)
struct UserSuggestionViewStateItem: Identifiable {
    let id: String
    let avatar: AvatarInputProtocol?
    let displayName: String?
}

@available(iOS 14.0, *)
struct UserSuggestionViewState: BindableState {
    var items: [UserSuggestionViewStateItem]
}
