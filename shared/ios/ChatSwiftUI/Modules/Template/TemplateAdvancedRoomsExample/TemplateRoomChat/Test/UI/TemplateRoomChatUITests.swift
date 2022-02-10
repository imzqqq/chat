import XCTest
import ChatSwiftUI

@available(iOS 14.0, *)
class TemplateRoomChatUITests: MockScreenTest {
    
    override class var screenType: MockScreenState.Type {
        return MockTemplateRoomChatScreenState.self
    }

    override class func createTest() -> MockScreenTest {
        return TemplateRoomChatUITests(selector: #selector(verifyTemplateRoomChatScreen))
    }
    
    func verifyTemplateRoomChatScreen() throws {
        guard let screenState = screenState as? MockTemplateRoomChatScreenState else { fatalError("no screen") }
        switch screenState {
        case .initializingRoom:
            verifyInitializingRoom()
        case .failedToInitializeRoom:
            verifyFailedToInitializeRoom()
        case .noMessages:
            verifyNoMessages()
        case .messages:
            verifyMessages()
        }
    }
    
    func verifyInitializingRoom() {
        let loadingProgress = app.activityIndicators["loadingProgress"]
        XCTAssert(loadingProgress.exists)
    }
    
    func verifyFailedToInitializeRoom() {
        let errorMessage = app.staticTexts["errorMessage"]
        XCTAssert(errorMessage.exists)
    }
    
    func verifyNoMessages() {
        let errorMessage = app.staticTexts["errorMessage"]
        XCTAssert(errorMessage.exists)
    }
    
    func verifyMessages() {
        // Verify bubble grouping with:
        // 3 bubbles
        let bubbleCount = app.images.matching(identifier:"bubbleImage").count
        XCTAssertEqual(bubbleCount, 3)
        
        // and 4 text items
        let bubbleTextItemCount = app.staticTexts.matching(identifier:"bubbleTextContent").count
        XCTAssertEqual(bubbleTextItemCount, 4)
        
        
    }

}
