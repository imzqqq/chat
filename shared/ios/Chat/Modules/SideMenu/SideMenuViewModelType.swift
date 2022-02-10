// File created from ScreenTemplate
// $ createScreen.sh SideMenu SideMenu

import Foundation

protocol SideMenuViewModelViewDelegate: AnyObject {
    func sideMenuViewModel(_ viewModel: SideMenuViewModelType, didUpdateViewState viewSate: SideMenuViewState)
}

protocol SideMenuViewModelCoordinatorDelegate: AnyObject {
    func sideMenuViewModel(_ viewModel: SideMenuViewModelType, didTapMenuItem menuItem: SideMenuItem, fromSourceView sourceView: UIView)
}

/// Protocol describing the view model used by `SideMenuViewController`
protocol SideMenuViewModelType {        
        
    var viewDelegate: SideMenuViewModelViewDelegate? { get set }
    var coordinatorDelegate: SideMenuViewModelCoordinatorDelegate? { get set }
    
    func process(viewAction: SideMenuViewAction)
}
