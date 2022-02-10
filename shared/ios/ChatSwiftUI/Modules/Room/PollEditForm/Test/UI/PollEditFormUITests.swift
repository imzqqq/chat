import XCTest
import ChatSwiftUI

@available(iOS 14.0, *)
class PollEditFormUITests: XCTestCase {
    
    private var app: XCUIApplication!
    
    override func setUp() {
        continueAfterFailure = false
                
        app = XCUIApplication()
        app.launch()
        app.buttons[MockPollEditFormScreenState.screenStateKeys.first!].tap()
    }
    
    func testInitialStateComponents() {
        
        XCTAssert(app.scrollViews.firstMatch.exists)
        
        XCTAssert(app.staticTexts["Create poll"].exists)
        XCTAssert(app.staticTexts["Poll question or topic"].exists)
        XCTAssert(app.staticTexts["Question or topic"].exists)
        XCTAssert(app.staticTexts["Create options"].exists)
        
        XCTAssert(app.textViews.count == 1)
        
        XCTAssert(app.textFields.count == 2)
        XCTAssert(app.staticTexts["Option 1"].exists)
        XCTAssert(app.staticTexts["Option 2"].exists)
        
        let cancelButton = app.buttons["Cancel"]
        XCTAssert(cancelButton.exists)
        XCTAssertTrue(cancelButton.isEnabled)
        
        let addOptionButton = app.buttons["Add option"]
        XCTAssert(addOptionButton.exists)
        XCTAssertTrue(addOptionButton.isEnabled)
        
        let createPollButton = app.buttons["Create poll"]
        XCTAssert(createPollButton.exists)
        XCTAssertFalse(createPollButton.isEnabled)
    }
    
    func testRemoveAddAnswerOptions() {
        
        let deleteAnswerOptionButton = app.buttons["Delete answer option"].firstMatch
        
        XCTAssert(deleteAnswerOptionButton.waitForExistence(timeout: 2.0))
        deleteAnswerOptionButton.tap()
        
        XCTAssert(deleteAnswerOptionButton.waitForExistence(timeout: 2.0))
        deleteAnswerOptionButton.tap()
        
        let addOptionButton = app.buttons["Add option"]
        XCTAssert(addOptionButton.waitForExistence(timeout: 2.0))
        XCTAssertTrue(addOptionButton.isEnabled)
        
        for i in 1...3 {
            addOptionButton.tap()
            XCTAssert(app.staticTexts["Option \(i)"].waitForExistence(timeout: 2.0))
        }
    }
}
