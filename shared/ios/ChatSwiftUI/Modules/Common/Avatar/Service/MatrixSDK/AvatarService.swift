import Foundation
import MatrixSDK
import Combine
import DesignKit

enum AvatarServiceError: Error {
    case pathNotfound
    case loadingImageFailed(Error?)
}

class AvatarService: AvatarServiceProtocol {
    
    private enum Constants {
        static let mimeType = "image/jpeg"
        static let thumbnailMethod = MXThumbnailingMethodCrop
    }
    
    private let mediaManager: MXMediaManager
    
    static func instantiate(mediaManager: MXMediaManager) -> AvatarServiceProtocol {
        return AvatarService(mediaManager: mediaManager)
    }
    
    init(mediaManager: MXMediaManager) {
        self.mediaManager = mediaManager
    }
    
    /// Given an mxContentUri, this function returns a Future of UIImage.
    ///
    /// If possible it will retrieve the image from network or cache, otherwise it will error.
    /// - Parameters:
    ///   - mxContentUri: matrix uri of the avatar to fetch
    ///   - avatarSize: The size of avatar to retrieve as defined in the DesignKit spec.
    /// - Returns: A Future of UIImage that returns an error if it fails to fetch the image.
    @available(iOS 14.0, *)
    func avatarImage(mxContentUri: String, avatarSize: AvatarSize) -> Future<UIImage, Error> {
        
        let cachePath = MXMediaManager.thumbnailCachePath(
            forMatrixContentURI: mxContentUri,
            andType: Constants.mimeType,
            inFolder: nil, 
            toFitViewSize: avatarSize.size,
            with: Constants.thumbnailMethod)
        
        return Future<UIImage, Error> { promise in
            if let image = MXMediaManager.loadThroughCache(withFilePath: cachePath),
               let imageUp = Self.orientImageUp(image: image) {
                // Already cached return avatar
                promise(.success(imageUp))
            }
        
            self.mediaManager.downloadThumbnail(
                fromMatrixContentURI: mxContentUri,
                withType: Constants.mimeType,
                inFolder: nil,
                toFitViewSize: avatarSize.size,
                with: Constants.thumbnailMethod) { path in
                guard let path = path else {
                    promise(.failure(AvatarServiceError.pathNotfound))
                    return
                }
                
                guard let image = MXMediaManager.loadThroughCache(withFilePath: path),
                      let imageUp = Self.orientImageUp(image: image) else {
                    promise(.failure(AvatarServiceError.loadingImageFailed(nil)))
                    return
                }
                promise(.success(imageUp))
            } failure: { error in
                promise(.failure(AvatarServiceError.loadingImageFailed(error)))
            }
        }
    }
    
    private static func orientImageUp(image: UIImage) -> UIImage? {
        guard let image = image.cgImage else { return nil }
        return UIImage(cgImage: image, scale: 1.0, orientation: .up)
    }
}
