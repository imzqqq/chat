import Foundation

/// SpaceListViewCell view data
struct SpaceListItemViewData {
    let spaceId: String
    let title: String
    let avatarViewData: AvatarViewDataProtocol
    let isInvite: Bool
    let notificationCount: UInt
    let highlightedNotificationCount: UInt
}
