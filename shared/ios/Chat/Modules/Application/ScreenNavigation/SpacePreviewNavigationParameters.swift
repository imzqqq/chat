import Foundation

/// Navigation parameters to display a preview of a space that is unknown for the user.
@objcMembers
class SpacePreviewNavigationParameters: SpaceNavigationParameters {
    
    // MARK: - Properties

    /// The data for the room preview
    let publicRoom: MXPublicRoom
    
    // MARK: - Setup
    
    init(publicRoom: MXPublicRoom,
         mxSession: MXSession,
         presentationParameters: ScreenPresentationParameters) {
        self.publicRoom = publicRoom
        
        super.init(roomId: publicRoom.roomId,
                   mxSession: mxSession,
                   presentationParameters: presentationParameters)
    }
}
