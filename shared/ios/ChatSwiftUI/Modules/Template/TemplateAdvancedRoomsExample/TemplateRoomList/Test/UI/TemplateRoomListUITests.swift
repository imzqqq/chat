import XCTest
import ChatSwiftUI

@available(iOS 14.0, *)
class TemplateRoomListUITests: MockScreenTest {
    
    override class var screenType: MockScreenState.Type {
        return MockTemplateRoomListScreenState.self
    }

    override class func createTest() -> MockScreenTest {
        return TemplateRoomListUITests(selector: #selector(verifyTemplateRoomListScreen))
    }
    
    func verifyTemplateRoomListScreen() throws {
        guard let screenState = screenState as? MockTemplateRoomListScreenState else { fatalError("no screen") }
        switch screenState {
        case .noRooms:
            verifyTemplateRoomListNoRooms()
        case .rooms:
            verifyTemplateRoomListRooms()
        }
    }
    
    func verifyTemplateRoomListNoRooms() {
        let errorMessage = app.staticTexts["errorMessage"]
        XCTAssert(errorMessage.exists)
        XCTAssert(errorMessage.label == "No Rooms")
    }
    
    func verifyTemplateRoomListRooms() {
        let displayNameCount = app.buttons.matching(identifier:"roomNameText").count
        XCTAssertEqual(displayNameCount, 3)
    }

}
