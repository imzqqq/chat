// File created from ScreenTemplate
// $ createScreen.sh Room2/RoomInfo RoomInfoList

import Foundation

final class RoomInfoListViewModel: NSObject, RoomInfoListViewModelType {
    
    // MARK: - Properties
    
    // MARK: Private

    private let session: MXSession
    private let room: MXRoom
    
    // MARK: Public

    weak var viewDelegate: RoomInfoListViewModelViewDelegate?
    weak var coordinatorDelegate: RoomInfoListViewModelCoordinatorDelegate?
    
    private var viewData: RoomInfoListViewData {
        let encryptionImage = EncryptionTrustLevelBadgeImageHelper.roomBadgeImage(for: room.summary.roomEncryptionTrustLevel())
        
        let basicInfoViewData = RoomInfoBasicViewData(avatarUrl: room.summary.avatar,
                                                      mediaManager: session.mediaManager,
                                                      roomId: room.roomId,
                                                      roomDisplayName: room.summary.displayname,
                                                      mainRoomAlias: room.summary.aliases?.first,
                                                      roomTopic: room.summary.topic,
                                                      encryptionImage: encryptionImage,
                                                      isEncrypted: room.summary.isEncrypted,
                                                      isDirect: room.isDirect)
        
        return RoomInfoListViewData(numberOfMembers: Int(room.summary.membersCount.joined),
                                    basicInfoViewData: basicInfoViewData)
    }
    
    // MARK: - Setup
    
    init(session: MXSession, room: MXRoom) {
        self.session = session
        self.room = room
        super.init()
        startObservingSummaryChanges()
    }
    
    deinit {
        stopObservingSummaryChanges()
    }
    
    // MARK: - Public
    
    func process(viewAction: RoomInfoListViewAction) {
        switch viewAction {
        case .loadData:
            self.loadData()
        case .navigate(let target):
            self.navigate(to: target)
        case .leave:
            self.leave()
        case .cancel:
            self.coordinatorDelegate?.roomInfoListViewModelDidCancel(self)
        }
    }
    
    // MARK: - Private
    
    private func startObservingSummaryChanges() {
        NotificationCenter.default.addObserver(self, selector: #selector(roomSummaryUpdated(_:)), name: .mxRoomSummaryDidChange, object: room.summary)
    }
    
    private func stopObservingSummaryChanges() {
        NotificationCenter.default.removeObserver(self, name: .mxRoomSummaryDidChange, object: nil)
    }
    
    @objc private func roomSummaryUpdated(_ notification: Notification) {
        //  force update view
        self.update(viewState: .loaded(viewData: viewData))
    }
    
    private func loadData() {
        self.update(viewState: .loaded(viewData: viewData))
    }
    
    private func navigate(to target: RoomInfoListTarget) {
        self.coordinatorDelegate?.roomInfoListViewModel(self, wantsToNavigateTo: target)
    }
    
    private func leave() {
        self.stopObservingSummaryChanges()
        self.update(viewState: .loading)
        self.room.leave { (response) in
            switch response {
            case .success:
                self.coordinatorDelegate?.roomInfoListViewModelDidLeaveRoom(self)
            case .failure(let error):
                self.startObservingSummaryChanges()
                self.update(viewState: .error(error))
            }
        }
    }
    
    private func update(viewState: RoomInfoListViewState) {
        self.viewDelegate?.roomInfoListViewModel(self, didUpdateViewState: viewState)
    }
}
