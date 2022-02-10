// File created from ScreenTemplate
// $ createScreen.sh CreateRoom/EnterNewRoomDetails EnterNewRoomDetails

import Foundation

/// EnterNewRoomDetailsViewController view state
enum EnterNewRoomDetailsViewState {
    case loading
    case loaded
    case error(Error)
}
