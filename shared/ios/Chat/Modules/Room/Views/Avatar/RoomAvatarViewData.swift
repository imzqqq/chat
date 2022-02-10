import Foundation

struct RoomAvatarViewData: AvatarViewDataProtocol {
    let roomId: String
    let displayName: String?
    let avatarUrl: String?
    let mediaManager: MXMediaManager?
    
    var matrixItemId: String {
        return roomId
    }
    
    var fallbackImage: AvatarFallbackImage? {
        return .matrixItem(matrixItemId, displayName)
    }
}
