// File created from ScreenTemplate
// $ createScreen.sh Rooms/ShowDirectory ShowDirectory

import Foundation

/// ShowDirectoryViewController view state
enum ShowDirectoryViewState {
    case loading
    case loadedWithoutUpdate
    case loaded(_ sections: [ShowDirectorySection])
    case error(Error)
}
