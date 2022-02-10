import UIKit

@objcMembers
final class EncryptionTrustLevelBadgeImageHelper: NSObject {
    
    static func roomBadgeImage(for trustLevel: RoomEncryptionTrustLevel) -> UIImage {
        
        let badgeImage: UIImage
        
        switch trustLevel {
        case .warning:
            badgeImage = Asset.Images.encryptionWarning.image
        case .normal:
            badgeImage = Asset.Images.encryptionNormal.image
        case .trusted:
            badgeImage = Asset.Images.encryptionTrusted.image
        case .unknown:
            badgeImage = Asset.Images.encryptionNormal.image
        @unknown default:
            badgeImage = Asset.Images.encryptionNormal.image
        }
        
        return badgeImage
    }
    
    static func userBadgeImage(for trustLevel: UserEncryptionTrustLevel) -> UIImage? {
        
        let badgeImage: UIImage?
        
        switch trustLevel {
        case .warning:
            badgeImage = Asset.Images.encryptionWarning.image
        case .notVerified, .noCrossSigning:
            badgeImage = Asset.Images.encryptionNormal.image
        case .trusted:
            badgeImage = Asset.Images.encryptionTrusted.image
        default:
            badgeImage = nil
        }
        
        return badgeImage
    }
}
