// File created from ScreenTemplate
// $ createScreen.sh ReactionHistory ReactionHistory

import Foundation

/// ReactionHistoryViewController view state
enum ReactionHistoryViewState {
    case loading
    case loaded(reactionHistoryViewDataList: [ReactionHistoryViewData], allDataLoaded: Bool)
    case error(Error)
}
