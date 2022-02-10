import XCTest
import ChatSwiftUI

@available(iOS 14.0, *)
class UserSuggestionUITests: MockScreenTest {
    
    override class var screenType: MockScreenState.Type {
        return MockUserSuggestionScreenState.self
    }

    override class func createTest() -> MockScreenTest {
        return UserSuggestionUITests(selector: #selector(verifyUserSuggestionScreen))
    }
    
    func verifyUserSuggestionScreen() throws {
        XCTAssert(app.tables.firstMatch.exists)
        
        let firstButton = app.tables.firstMatch.buttons.firstMatch
        _ = firstButton.waitForExistence(timeout: 10)
        XCTAssert(firstButton.identifier == "displayNameText-userIdText")
    }
}
