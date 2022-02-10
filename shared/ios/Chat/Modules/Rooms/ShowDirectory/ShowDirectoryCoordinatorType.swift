// File created from ScreenTemplate
// $ createScreen.sh Rooms/ShowDirectory ShowDirectory

import Foundation

protocol ShowDirectoryCoordinatorDelegate: AnyObject {
    func showDirectoryCoordinator(_ coordinator: ShowDirectoryCoordinatorType, didSelectRoom room: MXPublicRoom)
    func showDirectoryCoordinatorDidTapCreateNewRoom(_ coordinator: ShowDirectoryCoordinatorType)
    func showDirectoryCoordinatorDidCancel(_ coordinator: ShowDirectoryCoordinatorType)
    func showDirectoryCoordinatorWantsToShow(_ coordinator: ShowDirectoryCoordinatorType, viewController: UIViewController)
    func showDirectoryCoordinator(_ coordinator: ShowDirectoryCoordinatorType, didSelectRoomWithIdOrAlias roomIdOrAlias: String)
}

/// `ShowDirectoryCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol ShowDirectoryCoordinatorType: Coordinator, Presentable {
    var delegate: ShowDirectoryCoordinatorDelegate? { get }
}
