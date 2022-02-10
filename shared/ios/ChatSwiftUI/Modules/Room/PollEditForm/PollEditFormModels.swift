import Foundation
import SwiftUI

enum PollEditFormStateAction {
    case viewAction(PollEditFormViewAction)
}

enum PollEditFormViewAction {
    case addAnswerOption
    case deleteAnswerOption(PollEditFormAnswerOption)
    case cancel
    case create
}

enum PollEditFormViewModelResult {
    case cancel
    case create(String, [String])
}

struct PollEditFormQuestion {
    var text: String {
        didSet {
            text = String(text.prefix(maxLength))
        }
    }
    
    let maxLength: Int
}

struct PollEditFormAnswerOption: Identifiable, Equatable {
    let id = UUID()

    var text: String {
        didSet {
            text = String(text.prefix(maxLength))
        }
    }
    
    let maxLength: Int
}

struct PollEditFormViewState: BindableState {
    let maxAnswerOptionsCount: Int
    var bindings: PollEditFormViewStateBindings
    
    var confirmationButtonEnabled: Bool {
        !bindings.question.text.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty &&
            bindings.answerOptions.filter({ !$0.text.isEmpty }).count >= 2
    }
    
    var addAnswerOptionButtonEnabled: Bool {
        bindings.answerOptions.count < maxAnswerOptionsCount
    }
}

struct PollEditFormViewStateBindings {
    var question: PollEditFormQuestion
    var answerOptions: [PollEditFormAnswerOption]
}
