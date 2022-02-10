import SwiftUI
import Combine

@available(iOS 14, *)
typealias UserSuggestionViewModelType = StateStoreViewModel <UserSuggestionViewState,
                                                             UserSuggestionStateAction,
                                                             UserSuggestionViewAction>
@available(iOS 14, *)
class UserSuggestionViewModel: UserSuggestionViewModelType, UserSuggestionViewModelProtocol {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let userSuggestionService: UserSuggestionServiceProtocol
    
    // MARK: Public
    
    var completion: ((UserSuggestionViewModelResult) -> Void)?
    
    // MARK: - Setup

    static func makeUserSuggestionViewModel(userSuggestionService: UserSuggestionServiceProtocol) -> UserSuggestionViewModelProtocol {
        return UserSuggestionViewModel(userSuggestionService: userSuggestionService)
    }
    
    private init(userSuggestionService: UserSuggestionServiceProtocol) {
        self.userSuggestionService = userSuggestionService
        super.init(initialViewState: Self.defaultState(userSuggestionService: userSuggestionService))
        setupItemsObserving()
    }
    
    private func setupItemsObserving() {
        let updatePublisher = userSuggestionService.items
            .map(UserSuggestionStateAction.updateWithItems)
            .eraseToAnyPublisher()
        dispatch(actionPublisher: updatePublisher)
    }
    
    private static func defaultState(userSuggestionService: UserSuggestionServiceProtocol) -> UserSuggestionViewState {
        let viewStateItems = userSuggestionService.items.value.map { suggestionItem in
            return UserSuggestionViewStateItem(id: suggestionItem.userId, avatar: suggestionItem, displayName: suggestionItem.displayName)
        }
        
        return UserSuggestionViewState(items: viewStateItems)
    }
    
    // MARK: - Public
    
    override func process(viewAction: UserSuggestionViewAction) {
        switch viewAction {
        case .selectedItem(let item):
            completion?(.selectedItemWithIdentifier(item.id))
        }
    }
    
    override class func reducer(state: inout UserSuggestionViewState, action: UserSuggestionStateAction) {
        switch action {
        case .updateWithItems(let items):
            state.items = items.map({ item in
                UserSuggestionViewStateItem(id: item.userId, avatar: item, displayName: item.displayName)
            })
        }
    }
}
