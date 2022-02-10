import Foundation

enum BubbleReactionsViewAction {
    case loadData
    case tapReaction(index: Int)
    case addNewReaction
    case tapShowAction(action: ShowAction)
    case longPress

    enum ShowAction {
        case showAll
        case showLess
    }
}

enum BubbleReactionsViewState {
    case loaded(reactionsViewData: [BubbleReactionViewData], showAllButtonState: ShowAllButtonState)

    enum ShowAllButtonState {
        case none
        case showAll
        case showLess
    }
}

@objc protocol BubbleReactionsViewModelDelegate: AnyObject {
    func bubbleReactionsViewModel(_ viewModel: BubbleReactionsViewModel, didAddReaction reactionCount: MXReactionCount, forEventId eventId: String)
    func bubbleReactionsViewModel(_ viewModel: BubbleReactionsViewModel, didRemoveReaction reactionCount: MXReactionCount, forEventId eventId: String)
    func bubbleReactionsViewModel(_ viewModel: BubbleReactionsViewModel, didShowAllTappedForEventId eventId: String)
    func bubbleReactionsViewModel(_ viewModel: BubbleReactionsViewModel, didShowLessTappedForEventId eventId: String)
    func bubbleReactionsViewModel(_ viewModel: BubbleReactionsViewModel, didLongPressForEventId eventId: String)
}

protocol BubbleReactionsViewModelViewDelegate: AnyObject {
    func bubbleReactionsViewModel(_ viewModel: BubbleReactionsViewModel, didUpdateViewState viewState: BubbleReactionsViewState)
}

protocol BubbleReactionsViewModelType {
    var viewModelDelegate: BubbleReactionsViewModelDelegate? { get set }
    var viewDelegate: BubbleReactionsViewModelViewDelegate? { get set }
    
    func process(viewAction: BubbleReactionsViewAction)
}
