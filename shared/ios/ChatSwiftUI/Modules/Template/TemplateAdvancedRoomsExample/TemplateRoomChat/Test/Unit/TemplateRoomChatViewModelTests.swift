import XCTest
import Combine

@testable import ChatSwiftUI

@available(iOS 14.0, *)
class TemplateRoomChatViewModelTests: XCTestCase {

    var service: MockTemplateRoomChatService!
    var viewModel: TemplateRoomChatViewModel!
    var context: TemplateRoomChatViewModel.Context!
    var cancellables = Set<AnyCancellable>()
    
    override func setUpWithError() throws {
        service = MockTemplateRoomChatService()
        service.simulateUpdate(initializationStatus: .initialized)
        viewModel = TemplateRoomChatViewModel(templateRoomChatService: service)
        context = viewModel.context
    }
    
    func testInitialState() {
        XCTAssertEqual(context.viewState.bubbles.count, 3)
        XCTAssertEqual(context.viewState.sendButtonEnabled, false)
        XCTAssertEqual(context.viewState.roomInitializationStatus, .initialized)
    }


    func testSendMessageUpdatesReceived() throws {
        let bubblesPublisher: AnyPublisher<[[TemplateRoomChatBubble]], Never> = context.$viewState.map(\.bubbles).removeDuplicates().collect(2).first().eraseToAnyPublisher()
        let awaitDeferred = xcAwaitDeferred(bubblesPublisher)
        let newMessage: String = "Let's Go"
        service.send(textMessage: newMessage)
        
        let result: [[TemplateRoomChatBubble]]? = try awaitDeferred()
        
        // Test that the update to the messages in turn updates the view's
        // the last bubble by appending another text item, asserting the body.
        guard let item:TemplateRoomChatBubbleItem = result?.last?.last?.items.last,
              case TemplateRoomChatBubbleItemContent.message(let message) = item.content,
              case let TemplateRoomChatMessageContent.text(text) = message else {
            XCTFail()
            return
        }
        XCTAssertEqual(text.body, newMessage)
    }
}
