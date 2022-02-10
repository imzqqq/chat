// File created from ScreenTemplate
// $ createScreen.sh Rooms/ShowDirectory ShowDirectory

import Foundation

/// ShowDirectoryViewController view actions exposed to view model
enum ShowDirectoryViewAction {
    case loadData(_ force: Bool)
    case selectRoom(_ indexPath: IndexPath)
    case joinRoom(_ indexPath: IndexPath)
    case search(_ pattern: String?)
    case createNewRoom
    case switchServer
    case cancel
}
