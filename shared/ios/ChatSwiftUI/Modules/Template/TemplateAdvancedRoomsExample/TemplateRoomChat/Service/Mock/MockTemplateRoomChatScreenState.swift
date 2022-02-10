import Foundation
import SwiftUI


/// Using an enum for the screen allows you define the different state cases with
/// the relevant associated data for each case.
@available(iOS 14.0, *)
enum MockTemplateRoomChatScreenState: MockScreenState, CaseIterable {
    // A case for each state you want to represent
    // with specific, minimal associated data that will allow you
    // mock that screen.
    case initializingRoom
    case failedToInitializeRoom
    case noMessages
    case messages
    
    /// The associated screen
    var screenType: Any.Type {
        TemplateRoomChat.self
    }
    
    /// Generate the view struct for the screen state.
    var screenView: ([Any], AnyView) {
        let service: MockTemplateRoomChatService
        switch self {
        case .noMessages:
            service = MockTemplateRoomChatService(messages: [])
            service.simulateUpdate(initializationStatus: .initialized)
        case .messages:
            service = MockTemplateRoomChatService()
            service.simulateUpdate(initializationStatus: .initialized)
        case .initializingRoom:
            service = MockTemplateRoomChatService()
        case .failedToInitializeRoom:
            service = MockTemplateRoomChatService()
            service.simulateUpdate(initializationStatus: .failedToInitialize)
        }
        let viewModel = TemplateRoomChatViewModel(templateRoomChatService: service)
        
        // can simulate service and viewModel actions here if needs be.
        
        return (
            [service, viewModel],
            AnyView(TemplateRoomChat(viewModel: viewModel.context)
                        .addDependency(MockAvatarService.example))
        )
    }
}
