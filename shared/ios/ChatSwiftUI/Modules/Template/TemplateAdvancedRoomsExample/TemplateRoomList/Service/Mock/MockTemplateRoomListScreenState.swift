import Foundation
import SwiftUI


/// Using an enum for the screen allows you define the different state cases with
/// the relevant associated data for each case.
@available(iOS 14.0, *)
enum MockTemplateRoomListScreenState: MockScreenState, CaseIterable {
    // A case for each state you want to represent
    // with specific, minimal associated data that will allow you
    // mock that screen.
    case noRooms
    case rooms
    
    /// The associated screen
    var screenType: Any.Type {
        TemplateRoomList.self
    }
    
    /// Generate the view struct for the screen state.
    var screenView: ([Any], AnyView) {
        let service: MockTemplateRoomListService
        switch self {
        case .noRooms:
            service = MockTemplateRoomListService(rooms: [])
        case .rooms:
            service = MockTemplateRoomListService()
        }
        let viewModel = TemplateRoomListViewModel(templateRoomListService: service)
        
        // can simulate service and viewModel actions here if needs be.
        
        return (
            [service, viewModel],
            AnyView(TemplateRoomList(viewModel: viewModel.context)
                .addDependency(MockAvatarService.example))
        )
    }
}
