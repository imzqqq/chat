import Foundation
import MatrixKit

struct RoomInfoBasicViewData {
    let avatarUrl: String?
    let mediaManager: MXMediaManager?
    
    let roomId: String
    let roomDisplayName: String?
    let mainRoomAlias: String?
    let roomTopic: String?
    let encryptionImage: UIImage?
    let isEncrypted: Bool
    let isDirect: Bool
}
