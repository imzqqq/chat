import SwiftUI
import Combine
    
@available(iOS 14, *)
typealias TemplateRoomListViewModelType = StateStoreViewModel<TemplateRoomListViewState,
                                                              TemplateRoomListStateAction,
                                                              TemplateRoomListViewAction>
@available(iOS 14.0, *)
class TemplateRoomListViewModel: TemplateRoomListViewModelType, TemplateRoomListViewModelProtocol {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let templateRoomListService: TemplateRoomListServiceProtocol
    
    // MARK: Public
    
    var callback: ((TemplateRoomListViewModelAction) -> Void)?
    
    // MARK: - Setup
    
    init(templateRoomListService: TemplateRoomListServiceProtocol) {
        self.templateRoomListService = templateRoomListService
        super.init(initialViewState: Self.defaultState(templateRoomListService: templateRoomListService))
        startObservingRooms()
    }
    
    private static func defaultState(templateRoomListService: TemplateRoomListServiceProtocol) -> TemplateRoomListViewState {
        return TemplateRoomListViewState(rooms: templateRoomListService.roomsSubject.value)
    }
    
    private func startObservingRooms() {
        let roomsUpdatePublisher = templateRoomListService.roomsSubject
            .map(TemplateRoomListStateAction.updateRooms)
            .eraseToAnyPublisher()
        dispatch(actionPublisher: roomsUpdatePublisher)
    }
    
    // MARK: - Public
    
    override func process(viewAction: TemplateRoomListViewAction) {
        switch viewAction {
        case .didSelectRoom(let roomId):
            didSelect(by: roomId)
        case .done:
            done()
        }
    }
    
    override class func reducer(state: inout TemplateRoomListViewState, action: TemplateRoomListStateAction) {
        switch action {
        case .updateRooms(let rooms):
            state.rooms = rooms
        }
    }
    
    // MARK: - Private
    
    private func done() {
        callback?(.done)
    }
    
    private func didSelect(by roomId: String) {
        callback?(.didSelectRoom(roomId))
    }
}
