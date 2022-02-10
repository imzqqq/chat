// File created from ScreenTemplate
// $ createScreen.sh CreateRoom/EnterNewRoomDetails EnterNewRoomDetails

import Foundation

/// EnterNewRoomDetailsViewController view actions exposed to view model
enum EnterNewRoomDetailsViewAction {
    case loadData
    case chooseAvatar(sourceView: UIView)
    case cancel
    case create
}
