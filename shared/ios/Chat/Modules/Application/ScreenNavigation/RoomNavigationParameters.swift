import Foundation

/// Navigation parameters to display a room with a provided identifier in a specific matrix session.
@objcMembers
class RoomNavigationParameters: NSObject {
    
    // MARK: - Properties

    /// The room identifier
    let roomId: String
    
    /// If not nil, the room will be opened on this event.
    let eventId: String?
    
    /// The Matrix session in which the room should be available.
    let mxSession: MXSession
    
    /// Screen presentation parameters.
    let presentationParameters: ScreenPresentationParameters
    
    // MARK: - Setup
    
    init(roomId: String,
         eventId: String?,
         mxSession: MXSession,
         presentationParameters: ScreenPresentationParameters) {
        self.roomId = roomId
        self.eventId = eventId
        self.mxSession = mxSession
        self.presentationParameters = presentationParameters
        
        super.init()
    }
}
