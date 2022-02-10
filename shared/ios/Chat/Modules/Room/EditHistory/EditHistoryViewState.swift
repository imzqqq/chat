// File created from ScreenTemplate
// $ createScreen.sh Room/EditHistory EditHistory

import Foundation

/// EditHistoryViewController view state
enum EditHistoryViewState {
    case loading
    case loaded(sections: [EditHistorySection], addedCount: Int, allDataLoaded: Bool)
    case error(Error)
}
