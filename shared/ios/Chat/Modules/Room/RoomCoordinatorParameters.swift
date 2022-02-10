import Foundation

/// RoomCoordinator input parameters
struct RoomCoordinatorParameters {
    
    // MARK: - Properties
    
    /// The navigation router that manage physical navigation
    let navigationRouter: NavigationRouterType?
    
    /// The navigation router store that enables to get a NavigationRouter from a navigation controller
    /// `navigationRouter` property takes priority on `navigationRouterStore`
    let navigationRouterStore: NavigationRouterStoreProtocol?
    
    /// The matrix session in which the room should be available.
    let session: MXSession
    
    /// The room identifier
    let roomId: String
    
    /// If not nil, the room will be opened on this event.
    let eventId: String?
    
    /// The data for the room preview.
    let previewData: RoomPreviewData?
    
    // MARK: - Setup
    
    private init(navigationRouter: NavigationRouterType?,
                 navigationRouterStore: NavigationRouterStoreProtocol?,
                 session: MXSession,
                 roomId: String,
                 eventId: String?,
                 previewData: RoomPreviewData?) {
        self.navigationRouter = navigationRouter
        self.navigationRouterStore = navigationRouterStore
        self.session = session
        self.roomId = roomId
        self.eventId = eventId
        self.previewData = previewData
    }
    
    /// Init to present a joined room
    init(navigationRouter: NavigationRouterType? = nil,
         navigationRouterStore: NavigationRouterStoreProtocol? = nil,
         session: MXSession,
         roomId: String,
         eventId: String? = nil) {
        
        self.init(navigationRouter: navigationRouter, navigationRouterStore: navigationRouterStore, session: session, roomId: roomId, eventId: eventId, previewData: nil)
    }
    
    /// Init to present a room preview
    init(navigationRouter: NavigationRouterType? = nil,
         navigationRouterStore: NavigationRouterStoreProtocol? = nil,
         previewData: RoomPreviewData) {
        
        self.init(navigationRouter: navigationRouter, navigationRouterStore: navigationRouterStore, session: previewData.mxSession, roomId: previewData.roomId, eventId: nil, previewData: previewData)
    }
}
