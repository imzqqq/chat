import Foundation

class MockRoomNotificationSettingsService: RoomNotificationSettingsServiceType {
    
    static let example = MockRoomNotificationSettingsService(initialState: .all)
    
    var listener: RoomNotificationStateCallback?
    var notificationState: RoomNotificationState
    
    init(initialState: RoomNotificationState) {
        notificationState = initialState
    }
    
    func observeNotificationState(listener: @escaping RoomNotificationStateCallback) {
        self.listener = listener
    }
    
    func update(state: RoomNotificationState, completion: @escaping UpdateRoomNotificationStateCompletion) {
        self.notificationState = state
        completion()
        listener?(state)
    }
}
