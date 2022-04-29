import XCTest
import Combine

@testable import ChatSwiftUI

@available(iOS 14.0, *)
class UserSuggestionServiceTests: XCTestCase {
    
    var service: UserSuggestionService?
    
    override func setUp() {
        service = UserSuggestionService(roomMemberProvider: self, shouldDebounce: false)
    }
    
    func testAlice() {
        service?.processTextMessage("@Al")
        assert(service?.items.value.first?.displayName == "Alice")
        
        service?.processTextMessage("@al")
        assert(service?.items.value.first?.displayName == "Alice")
        
        service?.processTextMessage("@ice")
        assert(service?.items.value.first?.displayName == "Alice")
        
        service?.processTextMessage("@Alice")
        assert(service?.items.value.first?.displayName == "Alice")
        
        service?.processTextMessage("@alice:chat.imzqqq.top")
        assert(service?.items.value.first?.displayName == "Alice")
    }
    
    func testBob() {
        service?.processTextMessage("@ob")
        assert(service?.items.value.first?.displayName == "Bob")
        
        service?.processTextMessage("@ob:")
        assert(service?.items.value.first?.displayName == "Bob")
        
        service?.processTextMessage("@b:matrix")
        assert(service?.items.value.first?.displayName == "Bob")
    }
    
    func testBoth() {
        service?.processTextMessage("@:matrix")
        assert(service?.items.value.first?.displayName == "Alice")
        assert(service?.items.value.last?.displayName == "Bob")
        
        service?.processTextMessage("@.org")
        assert(service?.items.value.first?.displayName == "Alice")
        assert(service?.items.value.last?.displayName == "Bob")
    }
    
    func testEmptyResult() {
        service?.processTextMessage("Lorem ipsum idolor")
        assert(service?.items.value.count == 0)
        
        service?.processTextMessage("@")
        assert(service?.items.value.count == 0)
        
        service?.processTextMessage("@@")
        assert(service?.items.value.count == 0)
        
        service?.processTextMessage("alice@chat.imzqqq.top")
        assert(service?.items.value.count == 0)
    }
    
    func testStuff() {
        service?.processTextMessage("@@")
        assert(service?.items.value.count == 0)
    }
    
    func testWhitespaces() {
        service?.processTextMessage("")
        assert(service?.items.value.count == 0)
        
        service?.processTextMessage(" ")
        assert(service?.items.value.count == 0)
        
        service?.processTextMessage("\n")
        assert(service?.items.value.count == 0)
        
        service?.processTextMessage(" \n ")
        assert(service?.items.value.count == 0)
        
        service?.processTextMessage("@A   ")
        assert(service?.items.value.count == 0)
        
        service?.processTextMessage("  @A   ")
        assert(service?.items.value.count == 0)
    }
}

@available(iOS 14.0, *)
extension UserSuggestionServiceTests: RoomMembersProviderProtocol {
    func fetchMembers(_ members: @escaping ([RoomMembersProviderMember]) -> Void) {
        
        let users = [("Alice", "@alice:chat.imzqqq.top"),
                     ("Bob", "@bob:chat.imzqqq.top")]
        
        members(users.map({ user in
            RoomMembersProviderMember(userId: user.1, displayName: user.0, avatarUrl: "")
        }))
    }
}
