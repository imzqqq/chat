import SwiftUI
import Combine

@available(iOS 14, *)
typealias PollEditFormViewModelType = StateStoreViewModel< PollEditFormViewState,
                                                           PollEditFormStateAction,
                                                           PollEditFormViewAction >
@available(iOS 14, *)
class PollEditFormViewModel: PollEditFormViewModelType {
    
    private struct Constants {
        static let maxAnswerOptionsCount = 20
        static let maxQuestionLength = 200
        static let maxAnswerOptionLength = 200
    }

    // MARK: - Properties

    // MARK: Private
    
    // MARK: Public
    
    var completion: ((PollEditFormViewModelResult) -> Void)?
    
    // MARK: - Setup
    
    init() {
        super.init(initialViewState: Self.defaultState())
    }
    
    private static func defaultState() -> PollEditFormViewState {
        return PollEditFormViewState(
            maxAnswerOptionsCount: Constants.maxAnswerOptionsCount,
            bindings: PollEditFormViewStateBindings(
                question: PollEditFormQuestion(text: "", maxLength: Constants.maxQuestionLength),
                answerOptions: [PollEditFormAnswerOption(text: "", maxLength: Constants.maxAnswerOptionLength),
                                PollEditFormAnswerOption(text: "", maxLength: Constants.maxAnswerOptionLength)
                ]
            )
        )
    }
    
    // MARK: - Public
    
    override func process(viewAction: PollEditFormViewAction) {
        switch viewAction {
        case .cancel:
            completion?(.cancel)
        case .create:
            completion?(.create(state.bindings.question.text.trimmingCharacters(in: .whitespacesAndNewlines),
                                state.bindings.answerOptions.compactMap({ answerOption in
                                    let text = answerOption.text.trimmingCharacters(in: .whitespacesAndNewlines)
                                    return text.isEmpty ? nil : text
                                })))
        default:
            dispatch(action: .viewAction(viewAction))
        }
    }

    override class func reducer(state: inout PollEditFormViewState, action: PollEditFormStateAction) {
        switch action {
        case .viewAction(let viewAction):
            switch viewAction {
            case .deleteAnswerOption(let answerOption):
                state.bindings.answerOptions.removeAll { $0 == answerOption }
            case .addAnswerOption:
                state.bindings.answerOptions.append(PollEditFormAnswerOption(text: "", maxLength: Constants.maxAnswerOptionLength))
            default:
                break
            }
        }
    }
}
