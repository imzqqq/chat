import Foundation
import SwiftUI

@available(iOS 14.0, *)
enum MockUserSuggestionScreenState: MockScreenState, CaseIterable {
    case multipleResults
    
    static private var members: [RoomMembersProviderMember]!
    
    var screenType: Any.Type {
        MockUserSuggestionScreenState.self
    }
    
    var screenView: ([Any], AnyView)  {
        let service = UserSuggestionService(roomMemberProvider: self)
        let listViewModel = UserSuggestionViewModel.makeUserSuggestionViewModel(userSuggestionService: service)
        
        let viewModel = UserSuggestionListWithInputViewModel(listViewModel: listViewModel) { textMessage in
            service.processTextMessage(textMessage)
        }
        
        return (
            [service, listViewModel],
            AnyView(UserSuggestionListWithInput(viewModel: viewModel)
                        .addDependency(MockAvatarService.example))
        )
    }
}

@available(iOS 14.0, *)
extension MockUserSuggestionScreenState: RoomMembersProviderProtocol {
    func fetchMembers(_ members: ([RoomMembersProviderMember]) -> Void) {
        if Self.members == nil {
            Self.members = generateUsersWithCount(10)
        }
        
        members(Self.members)
    }
    
    private func generateUsersWithCount(_ count: UInt) -> [RoomMembersProviderMember] {
        return (0..<count).map { _ in
            let identifier = "@" + UUID().uuidString
            return RoomMembersProviderMember(userId: identifier, displayName: identifier, avatarUrl: "mxc://chat.dingshunyu.top/VyNYAgahaiAzUoOeZETtQ")
        }
    }
}
