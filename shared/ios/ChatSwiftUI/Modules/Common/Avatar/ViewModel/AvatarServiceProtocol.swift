import Foundation
import DesignKit
import Combine
import UIKit


/// Provides a simple api to retrieve and cache avatar images
protocol AvatarServiceProtocol {
    @available(iOS 14.0, *)
    func avatarImage(mxContentUri: String, avatarSize: AvatarSize) -> Future<UIImage, Error>
}
