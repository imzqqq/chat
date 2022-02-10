// File created from ScreenTemplate
// $ createScreen.sh Room/EditHistory EditHistory

import Foundation

protocol EditHistoryViewModelViewDelegate: AnyObject {
    func editHistoryViewModel(_ viewModel: EditHistoryViewModelType, didUpdateViewState viewSate: EditHistoryViewState)
}

protocol EditHistoryViewModelCoordinatorDelegate: AnyObject {
    func editHistoryViewModelDidClose(_ viewModel: EditHistoryViewModelType)
}

/// Protocol describing the view model used by `EditHistoryViewController`
protocol EditHistoryViewModelType {
        
    var viewDelegate: EditHistoryViewModelViewDelegate? { get set }
    var coordinatorDelegate: EditHistoryViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: EditHistoryViewAction)
}
