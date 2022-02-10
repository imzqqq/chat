import SwiftUI
import Combine



@available(iOS 14, *)
typealias TemplateUserProfileViewModelType = StateStoreViewModel<TemplateUserProfileViewState,
                                                                 TemplateUserProfileStateAction,
                                                                 TemplateUserProfileViewAction>
@available(iOS 14, *)
class TemplateUserProfileViewModel: TemplateUserProfileViewModelType, TemplateUserProfileViewModelProtocol {

    // MARK: - Properties

    // MARK: Private

    private let templateUserProfileService: TemplateUserProfileServiceProtocol

    // MARK: Public

    var completion: ((TemplateUserProfileViewModelResult) -> Void)?

    // MARK: - Setup

    static func makeTemplateUserProfileViewModel(templateUserProfileService: TemplateUserProfileServiceProtocol) -> TemplateUserProfileViewModelProtocol {
        return TemplateUserProfileViewModel(templateUserProfileService: templateUserProfileService)
    }

    private init(templateUserProfileService: TemplateUserProfileServiceProtocol) {
        self.templateUserProfileService = templateUserProfileService
        super.init(initialViewState: Self.defaultState(templateUserProfileService: templateUserProfileService))
        setupPresenceObserving()
    }

    private static func defaultState(templateUserProfileService: TemplateUserProfileServiceProtocol) -> TemplateUserProfileViewState {
        return TemplateUserProfileViewState(
            avatar: templateUserProfileService.avatarData,
            displayName: templateUserProfileService.displayName,
            presence: templateUserProfileService.presenceSubject.value,
            count: 0
        )
    }

    private func setupPresenceObserving() {
        let presenceUpdatePublisher = templateUserProfileService.presenceSubject
            .map(TemplateUserProfileStateAction.updatePresence)
            .eraseToAnyPublisher()
        dispatch(actionPublisher: presenceUpdatePublisher)
    }

    // MARK: - Public

    override func process(viewAction: TemplateUserProfileViewAction) {
        switch viewAction {
        case .cancel:
            cancel()
        case .done:
            done()
        case .incrementCount, .decrementCount:
            dispatch(action: .viewAction(viewAction))
        }
    }

    override class func reducer(state: inout TemplateUserProfileViewState, action: TemplateUserProfileStateAction) {
        switch action {
        case .updatePresence(let presence):
            state.presence = presence
        case .viewAction(let viewAction):
            switch viewAction {
            case .incrementCount:
                state.count += 1
            case .decrementCount:
                state.count -= 1
            case .cancel, .done:
                break
            }
        }
        UILog.debug("[TemplateUserProfileViewModel] reducer with action \(action) produced state: \(state)")
    }

    private func done() {
        completion?(.done)
    }

    private func cancel() {
        completion?(.cancel)
    }
}
