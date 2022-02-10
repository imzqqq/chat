import Foundation

struct SpaceDetailLoadedParameters {
    let spaceId: String
    let displayName: String?
    let topic: String?
    let avatarUrl: String?
    let joinRule: MXRoomJoinRule?
    let membership: MXMembership
    let inviterId: String?
    let inviter: MXUser?
    let membersCount: UInt
}

/// SpaceDetailViewController view state
enum SpaceDetailViewState {
    case loading
    case loaded(_ paremeters: SpaceDetailLoadedParameters)
    case error(Error)
}
