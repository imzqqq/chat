import Foundation
import Combine

@available(iOS 14.0, *)
class MockTemplateRoomListService: TemplateRoomListServiceProtocol {
    
    static let mockRooms = [
        TemplateRoomListRoom(id: "!aaabaa:chat.imzqqq.top", avatar: MockAvatarInput.example, displayName: "Matrix Discussion"),
        TemplateRoomListRoom(id: "!zzasds:chat.imzqqq.top", avatar: MockAvatarInput.example, displayName: "Element Mobile"),
        TemplateRoomListRoom(id: "!scthve:chat.imzqqq.top", avatar: MockAvatarInput.example, displayName: "Alice Personal")
    ]
    var roomsSubject: CurrentValueSubject<[TemplateRoomListRoom], Never>

    init(rooms: [TemplateRoomListRoom] = mockRooms) {
        roomsSubject = CurrentValueSubject(rooms)
    }
    
    func simulateUpdate(rooms: [TemplateRoomListRoom]) {
        self.roomsSubject.send(rooms)
    }
}
