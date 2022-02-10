import XCTest
import Combine

@testable import ChatSwiftUI

@available(iOS 14.0, *)
class PollEditFormViewModelTests: XCTestCase {
    var viewModel: PollEditFormViewModel!
    var context: PollEditFormViewModelType.Context!
    var cancellables = Set<AnyCancellable>()
    
    override func setUpWithError() throws {
        viewModel = PollEditFormViewModel()
        context = viewModel.context
    }

    func testInitialState() {
        XCTAssertTrue(context.question.text.isEmpty)
        XCTAssertFalse(context.viewState.confirmationButtonEnabled)
        XCTAssertTrue(context.viewState.addAnswerOptionButtonEnabled)
        
        XCTAssertEqual(context.answerOptions.count, 2)
        for answerOption in context.answerOptions {
            XCTAssertTrue(answerOption.text.isEmpty)
        }
    }
    
    func testDeleteAllAnswerOptions() {
        while !context.answerOptions.isEmpty {
            context.send(viewAction: .deleteAnswerOption(context.answerOptions.first!))
        }
        
        XCTAssertEqual(context.answerOptions.count, 0)
        XCTAssertFalse(context.viewState.confirmationButtonEnabled)
        XCTAssertTrue(context.viewState.addAnswerOptionButtonEnabled)
    }
    
    func testAddRemoveAnswerOption() {
        context.send(viewAction: .addAnswerOption)
        
        XCTAssertEqual(context.answerOptions.count, 3)
        
        context.send(viewAction: .deleteAnswerOption(context.answerOptions.first!))
        
        XCTAssertEqual(context.answerOptions.count, 2)
    }
    
    func testCreateEnabled() {
        context.question.text = "Some question"
        context.answerOptions[0].text = "First answer"
        context.answerOptions[1].text = "Second answer"
        
        XCTAssertTrue(context.viewState.confirmationButtonEnabled)
    }
    
    func testReachedMaxAnswerOptions() {
        for _ in 0...context.viewState.maxAnswerOptionsCount {
            context.send(viewAction: .addAnswerOption)
        }
        
        XCTAssertFalse(context.viewState.addAnswerOptionButtonEnabled)
    }
    
    func testQuestionMaxLength() {
        let question = String(repeating: "S", count: context.question.maxLength + 100)
        context.question.text = question
        
        XCTAssertEqual(context.question.text.count, context.question.maxLength)
    }
    
    func testAnswerOptionMaxLength() {
        let answerOption = String(repeating: "S", count: context.answerOptions[0].maxLength + 100)
        context.answerOptions[0].text = answerOption
        
        XCTAssertEqual(context.answerOptions[0].text.count, context.answerOptions[0].maxLength)
    }
    
    func testFormCompletion() {
        let question = "Some question     "
        let firstAnswer = "First answer   "
        let secondAnswer = "Second answer "
        let thirdAnswer = "        "
        
        viewModel.completion = { result in
            if case PollEditFormViewModelResult.create(let resultQuestion, let resultAnswerOptions) = result {
                XCTAssertEqual(question.trimmingCharacters(in: .whitespacesAndNewlines), resultQuestion)
                
                // The last answer option should be automatically dropped as it's empty
                XCTAssertEqual(resultAnswerOptions.count, 2)
                
                XCTAssertEqual(resultAnswerOptions[0], firstAnswer.trimmingCharacters(in: .whitespacesAndNewlines))
                XCTAssertEqual(resultAnswerOptions[1], secondAnswer.trimmingCharacters(in: .whitespacesAndNewlines))
            }
        }
        
        context.question.text = question
        context.answerOptions[0].text = firstAnswer
        context.answerOptions[1].text = secondAnswer
        
        context.send(viewAction: .addAnswerOption)
        context.answerOptions[2].text = thirdAnswer
        
        context.send(viewAction: .create)
    }
}
