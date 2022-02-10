import Foundation

/// SpaceChildRoomDetailViewController view state
enum SpaceChildRoomDetailViewState {
    case loading
    case loaded(_ roomInfo: MXSpaceChildInfo, _ avatarViewData: AvatarViewData, _ isJoined: Bool)
    case error(Error)
}
