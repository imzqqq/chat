// File created from ScreenTemplate
// $ createScreen.sh CreateRoom/EnterNewRoomDetails EnterNewRoomDetails

import Foundation

protocol EnterNewRoomDetailsCoordinatorDelegate: AnyObject {
    func enterNewRoomDetailsCoordinator(_ coordinator: EnterNewRoomDetailsCoordinatorType, didCreateNewRoom room: MXRoom)
    func enterNewRoomDetailsCoordinatorDidCancel(_ coordinator: EnterNewRoomDetailsCoordinatorType)
}

/// `EnterNewRoomDetailsCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol EnterNewRoomDetailsCoordinatorType: Coordinator, Presentable {
    var delegate: EnterNewRoomDetailsCoordinatorDelegate? { get }
}
