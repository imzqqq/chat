import Foundation

extension MXKImageView {
    @objc func vc_setRoomAvatarImage(with url: String?, roomId: String, displayName: String, mediaManager: MXMediaManager) {
        // Use the display name to prepare the default avatar image.
        let avatarImage = AvatarGenerator.generateAvatar(forMatrixItem: roomId, withDisplayName: displayName)

        if let avatarUrl = url {
            self.enableInMemoryCache = true
            self.setImageURI(avatarUrl, withType: nil, andImageOrientation: .up, toFitViewSize: self.frame.size, with: MXThumbnailingMethodCrop, previewImage: avatarImage, mediaManager: mediaManager)
        } else {
            self.image = avatarImage
        }
        self.contentMode = .scaleAspectFill
    }
}
