import Foundation
import UIKit
import SwiftUI

final class TemplateRoomListCoordinator: Coordinator, Presentable {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let parameters: TemplateRoomListCoordinatorParameters
    private let templateRoomListHostingController: UIViewController
    private var templateRoomListViewModel: TemplateRoomListViewModelProtocol
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    var callback: ((TemplateRoomListCoordinatorAction) -> Void)?
    
    // MARK: - Setup
    
    @available(iOS 14.0, *)
    init(parameters: TemplateRoomListCoordinatorParameters) {
        self.parameters = parameters
        let viewModel = TemplateRoomListViewModel(templateRoomListService: TemplateRoomListService(session: parameters.session))
        let view = TemplateRoomList(viewModel: viewModel.context)
            .addDependency(AvatarService.instantiate(mediaManager: parameters.session.mediaManager))
        templateRoomListViewModel = viewModel
        templateRoomListHostingController = VectorHostingController(rootView: view)
    }
    
    // MARK: - Public
    
    func start() {
        MXLog.debug("[TemplateRoomListCoordinator] did start.")
        templateRoomListViewModel.callback = { [weak self] result in
            MXLog.debug("[TemplateRoomListCoordinator] TemplateRoomListViewModel did complete with result \(result).")
            guard let self = self else { return }
            switch result {
            case .didSelectRoom(let roomId):
                self.callback?(.didSelectRoom(roomId))
            case .done:
                self.callback?(.done)
            break
            }
        }
    }
    
    func toPresentable() -> UIViewController {
        return self.templateRoomListHostingController
    }
}
