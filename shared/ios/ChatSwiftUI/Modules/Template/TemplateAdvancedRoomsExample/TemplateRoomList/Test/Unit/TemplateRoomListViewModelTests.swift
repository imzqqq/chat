import XCTest
import Combine

@testable import ChatSwiftUI

@available(iOS 14.0, *)
class TemplateRoomListViewModelTests: XCTestCase {
    private enum Constants {
    }
    var service: MockTemplateRoomListService!
    var viewModel: TemplateRoomListViewModel!
    var context: TemplateRoomListViewModel.Context!
    var cancellables = Set<AnyCancellable>()
    
    override func setUpWithError() throws {
        service = MockTemplateRoomListService()
        viewModel = TemplateRoomListViewModel(templateRoomListService: service)
        context = viewModel.context
    }
    
    func testInitialState() {
        XCTAssertEqual(context.viewState.rooms, MockTemplateRoomListService.mockRooms)
    }

    func testFirstValueReceived() throws {
        let roomsPublisher = context.$viewState.map(\.rooms).removeDuplicates().collect(1).first()
        XCTAssertEqual(try xcAwait(roomsPublisher), [MockTemplateRoomListService.mockRooms])
    }
    
    func testUpdatesReceived() throws {
        let updatedRooms = Array(MockTemplateRoomListService.mockRooms.dropLast())
        let roomsPublisher = context.$viewState.map(\.rooms).removeDuplicates().collect(2).first()
        let awaitDeferred = xcAwaitDeferred(roomsPublisher)
        service.simulateUpdate(rooms:  updatedRooms)
        XCTAssertEqual(try awaitDeferred(), [MockTemplateRoomListService.mockRooms, updatedRooms])
    }
}
