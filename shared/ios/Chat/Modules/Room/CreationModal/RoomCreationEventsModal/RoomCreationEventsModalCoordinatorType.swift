// File created from ScreenTemplate
// $ createScreen.sh Modal2/RoomCreation RoomCreationEventsModal

import Foundation

protocol RoomCreationEventsModalCoordinatorDelegate: AnyObject {
    func roomCreationEventsModalCoordinatorDidTapClose(_ coordinator: RoomCreationEventsModalCoordinatorType)
}

/// `RoomCreationEventsModalCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol RoomCreationEventsModalCoordinatorType: Coordinator, Presentable {
    var delegate: RoomCreationEventsModalCoordinatorDelegate? { get }
}
