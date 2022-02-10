// File created from ScreenTemplate
// $ createScreen.sh SideMenu SideMenu

import Foundation

protocol SideMenuCoordinatorDelegate: AnyObject {
    func sideMenuCoordinator(_ coordinator: SideMenuCoordinatorType, didTapMenuItem menuItem: SideMenuItem, fromSourceView sourceView: UIView)
}

/// `SideMenuCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol SideMenuCoordinatorType: Coordinator, Presentable {
    var delegate: SideMenuCoordinatorDelegate? { get }
    
    @discardableResult func addScreenEdgePanGesturesToPresent(to view: UIView) -> UIScreenEdgePanGestureRecognizer
    @discardableResult func addPanGestureToPresent(to view: UIView) -> UIPanGestureRecognizer
    func select(spaceWithId spaceId: String)
}
