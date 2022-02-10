// File created from FlowTemplate
// $ createRootCoordinator.sh CreateRoom CreateRoom EnterNewRoomDetails

import UIKit

@objcMembers
final class CreateRoomCoordinator: CreateRoomCoordinatorType {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let navigationRouter: NavigationRouterType
    private let session: MXSession
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    
    weak var delegate: CreateRoomCoordinatorDelegate?
    
    // MARK: - Setup
    
    init(session: MXSession) {
        self.navigationRouter = NavigationRouter(navigationController: ChatNavigationController())
        self.session = session
    }
    
    // MARK: - Public methods
    
    func start() {

        let rootCoordinator = self.createEnterNewRoomDetailsCoordinator()

        rootCoordinator.start()

        self.add(childCoordinator: rootCoordinator)

        self.navigationRouter.setRootModule(rootCoordinator)
      }
    
    func toPresentable() -> UIViewController {
        return self.navigationRouter.toPresentable()
    }
    
    // MARK: - Private methods

    private func createEnterNewRoomDetailsCoordinator() -> EnterNewRoomDetailsCoordinator {
        let coordinator = EnterNewRoomDetailsCoordinator(session: self.session)
        coordinator.delegate = self
        return coordinator
    }
}

// MARK: - EnterNewRoomDetailsCoordinatorDelegate
extension CreateRoomCoordinator: EnterNewRoomDetailsCoordinatorDelegate {
    
    func enterNewRoomDetailsCoordinator(_ coordinator: EnterNewRoomDetailsCoordinatorType, didCreateNewRoom room: MXRoom) {
        self.delegate?.createRoomCoordinator(self, didCreateNewRoom: room)
    }
    
    func enterNewRoomDetailsCoordinatorDidCancel(_ coordinator: EnterNewRoomDetailsCoordinatorType) {
        self.delegate?.createRoomCoordinatorDidCancel(self)
    }
}
