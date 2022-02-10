import XCTest
import ChatSwiftUI

@available(iOS 14.0, *)
class TemplateUserProfileUITests: MockScreenTest {

    override class var screenType: MockScreenState.Type {
        return MockTemplateUserProfileScreenState.self
    }

    override class func createTest() -> MockScreenTest {
        return TemplateUserProfileUITests(selector: #selector(verifyTemplateUserProfileScreen))
    }

    func verifyTemplateUserProfileScreen() throws {
        guard let screenState = screenState as? MockTemplateUserProfileScreenState else { fatalError("no screen") }
        switch screenState {
        case .presence(let presence):
            verifyTemplateUserProfilePresence(presence: presence)
        case .longDisplayName(let name):
            verifyTemplateUserProfileLongName(name: name)
        }
    }

    func verifyTemplateUserProfilePresence(presence: TemplateUserProfilePresence) {
        let presenceText = app.staticTexts["presenceText"]
        XCTAssert(presenceText.exists)
        XCTAssertEqual(presenceText.label, presence.title)
    }

    func verifyTemplateUserProfileLongName(name: String) {
        let displayNameText = app.staticTexts["displayNameText"]
        XCTAssert(displayNameText.exists)
        XCTAssertEqual(displayNameText.label, name)
    }

}
