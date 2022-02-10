// File created from ScreenTemplate
// $ createScreen.sh ReactionHistory ReactionHistory

import Foundation

protocol ReactionHistoryViewModelViewDelegate: AnyObject {
    func reactionHistoryViewModel(_ viewModel: ReactionHistoryViewModelType, didUpdateViewState viewSate: ReactionHistoryViewState)
}

protocol ReactionHistoryViewModelCoordinatorDelegate: AnyObject {        
    func reactionHistoryViewModelDidClose(_ viewModel: ReactionHistoryViewModelType)
}

/// Protocol describing the view model used by `ReactionHistoryViewController`
protocol ReactionHistoryViewModelType {    
        
    var viewDelegate: ReactionHistoryViewModelViewDelegate? { get set }
    var coordinatorDelegate: ReactionHistoryViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: ReactionHistoryViewAction)
}
