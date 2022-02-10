import Foundation

/// The static list of mocked screens in ChatSwiftUI
@available(iOS 14.0, *)
enum MockAppScreens {
    static let appScreens: [MockScreenState.Type] = [
        MockTemplateUserProfileScreenState.self,
        MockTemplateRoomListScreenState.self,
        MockTemplateRoomChatScreenState.self,
        MockUserSuggestionScreenState.self,
        MockPollEditFormScreenState.self
    ]
}

