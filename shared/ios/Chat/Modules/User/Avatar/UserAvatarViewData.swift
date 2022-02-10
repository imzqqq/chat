import Foundation

struct UserAvatarViewData: AvatarViewDataProtocol {
    let userId: String
    let displayName: String?
    let avatarUrl: String?
    let mediaManager: MXMediaManager?
    
    var matrixItemId: String {
        return userId
    }
    
    var fallbackImage: AvatarFallbackImage? {
        return .matrixItem(matrixItemId, displayName)
    }
}
