import XCTest
import ChatSwiftUI

/// XCTestCase subclass to ease testing of `MockScreenState`.
/// Creates a test case for each screen state, launches the app,
/// goes to the correct screen and provides the state and key for each
/// invocation of the test.
@available(iOS 14.0, *)
class MockScreenTest: XCTestCase {
    
    enum Constants {
        static let defaultTimeout: TimeInterval = 3
    }
    
    class var screenType: MockScreenState.Type? {
        return nil
    }
    
    class func createTest() -> MockScreenTest {
        return MockScreenTest()
    }
    
    var screenState: MockScreenState?
    var screenStateKey: String?
    let app = XCUIApplication()
        
    override class var defaultTestSuite: XCTestSuite {
        let testSuite = XCTestSuite(name: NSStringFromClass(self))
        guard let screenType = screenType else {
            return testSuite
        }
        // Create a test case for each screen state
        screenType.screenStates.enumerated().forEach { index, screenState in
            let key = screenType.screenStateKeys[index]
            addTestFor(screenState: screenState, screenStateKey: key, toTestSuite: testSuite)
        }
        return testSuite
    }
    
    class func addTestFor(screenState: MockScreenState, screenStateKey: String, toTestSuite testSuite: XCTestSuite) {
        let test = createTest()
        test.screenState = screenState
        test.screenStateKey = screenStateKey
        testSuite.addTest(test)
    }
    
    open override func setUpWithError() throws {
        // For every test case launch the app and go to the relevant screen
        continueAfterFailure = false
        app.launch()
        goToScreen()
    }
    
    private func goToScreen() {
        guard let screenKey = screenStateKey else { fatalError("no screen") }
        let link = app.buttons[screenKey]
        link.tap()
    }
}
