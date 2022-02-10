import Foundation
import Combine
import DesignKit
import UIKit

@available(iOS 14.0, *)
class MockAvatarService: AvatarServiceProtocol {
    static let example: AvatarServiceProtocol = MockAvatarService()
    func avatarImage(mxContentUri: String, avatarSize: AvatarSize) -> Future<UIImage, Error> {
        Future { promise in
            promise(.success(Asset.Images.appSymbol.image))
        }
    }
}
