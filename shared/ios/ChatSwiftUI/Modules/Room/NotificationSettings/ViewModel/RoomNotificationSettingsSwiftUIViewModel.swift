import Foundation
import Combine

@available(iOS 14.0, *)
class RoomNotificationSettingsSwiftUIViewModel: RoomNotificationSettingsViewModel, ObservableObject {

    @Published var viewState: RoomNotificationSettingsViewState
    
    lazy var cancellables = Set<AnyCancellable>()
    
    override init(roomNotificationService: RoomNotificationSettingsServiceType, initialState: RoomNotificationSettingsViewState) {
        self.viewState = initialState
        super.init(roomNotificationService: roomNotificationService, initialState: initialState)
    }
    
    override func update(viewState: RoomNotificationSettingsViewState) {
        super.update(viewState: viewState)
        self.viewState = viewState
    }
}
