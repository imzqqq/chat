import Foundation
import UIKit
import SwiftUI

final class TemplateUserProfileCoordinator: Coordinator {
    
    // MARK: - Properties
    
    // MARK: Private
    
    private let parameters: TemplateUserProfileCoordinatorParameters
    private let templateUserProfileHostingController: UIViewController
    private var templateUserProfileViewModel: TemplateUserProfileViewModelProtocol
    
    // MARK: Public

    // Must be used only internally
    var childCoordinators: [Coordinator] = []
    var completion: (() -> Void)?
    
    // MARK: - Setup
    
    @available(iOS 14.0, *)
    init(parameters: TemplateUserProfileCoordinatorParameters) {
        self.parameters = parameters
        let viewModel = TemplateUserProfileViewModel.makeTemplateUserProfileViewModel(templateUserProfileService: TemplateUserProfileService(session: parameters.session))
        let view = TemplateUserProfile(viewModel: viewModel.context)
            .addDependency(AvatarService.instantiate(mediaManager: parameters.session.mediaManager))
        templateUserProfileViewModel = viewModel
        templateUserProfileHostingController = VectorHostingController(rootView: view)
    }
    
    // MARK: - Public
    func start() {
        MXLog.debug("[TemplateUserProfileCoordinator] did start.")
        templateUserProfileViewModel.completion = { [weak self] result in
            MXLog.debug("[TemplateUserProfileCoordinator] TemplateUserProfileViewModel did complete with result: \(result).")
            guard let self = self else { return }
            switch result {
            case .cancel, .done:
                self.completion?()
            }
        }
    }
    
    func toPresentable() -> UIViewController {
        return self.templateUserProfileHostingController
    }
}
