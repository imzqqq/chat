// File created from ScreenTemplate
// $ createScreen.sh Spaces/SpaceRoomList/SpaceChildRoomDetail ShowSpaceChildRoomDetail
import Foundation

final class SpaceChildRoomDetailViewModel: SpaceChildRoomDetailViewModelType {
    
    // MARK: - Properties
    
    // MARK: Private

    private let session: MXSession
    private let childInfo: MXSpaceChildInfo
    
    private var currentOperation: MXHTTPOperation?
    private var userDisplayName: String?
    private var isRoomJoined: Bool {
        let summary = self.session.roomSummary(withRoomId: self.childInfo.childRoomId)
        return summary?.isJoined ?? false
    }
    
    // MARK: Public

    weak var viewDelegate: SpaceChildRoomDetailViewModelViewDelegate?
    weak var coordinatorDelegate: SpaceChildRoomDetailViewModelCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(parameters: SpaceChildRoomDetailCoordinatorParameters) {
        self.session = parameters.session
        self.childInfo = parameters.childInfo
    }
    
    deinit {
        self.cancelOperations()
    }
    
    // MARK: - Public
    
    func process(viewAction: SpaceChildRoomDetailViewAction) {
        switch viewAction {
        case .loadData:
            self.loadData()
        case .complete:
            if self.isRoomJoined {
                self.coordinatorDelegate?.spaceChildRoomDetailViewModel(self, didOpenRoomWith: self.childInfo.childRoomId)
            } else {
                joinRoom()
            }
        case .cancel:
            self.cancelOperations()
            self.coordinatorDelegate?.spaceChildRoomDetailViewModelDidCancel(self)
        }
    }
    
    // MARK: - Private
    
    private func loadData() {
        let avatarViewData = AvatarViewData(matrixItemId: self.childInfo.childRoomId, displayName: self.childInfo.displayName, avatarUrl: self.childInfo.avatarUrl, mediaManager: self.session.mediaManager, fallbackImage: .matrixItem(self.childInfo.childRoomId, self.childInfo.name))
        self.update(viewState: .loaded(self.childInfo, avatarViewData, self.isRoomJoined))
    }
    
    private func update(viewState: SpaceChildRoomDetailViewState) {
        self.viewDelegate?.spaceChildRoomDetailViewModel(self, didUpdateViewState: viewState)
    }
    
    private func cancelOperations() {
        self.currentOperation?.cancel()
    }
    
    private func joinRoom() {
        self.update(viewState: .loading)
        self.session.joinRoom(self.childInfo.childRoomId) { [weak self] (response) in
            guard let self = self else { return }
            switch response {
            case .success:
                self.loadData()
            case .failure(let error):
                self.update(viewState: .error(error))
            }
        }
    }
}
