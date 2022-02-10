import UIKit

protocol SplitViewMasterPresentableDelegate: AnyObject {
    
    /// Detail items from the split view
    var detailModules: [Presentable] { get }
    
    /// Replace split view detail with the given detailPresentable
    func splitViewMasterPresentable(_ presentable: Presentable, wantsToReplaceDetailWith detailPresentable: Presentable, popCompletion: (() -> Void)?)
    
    /// Stack the detailPresentable on the existing split view detail stack
    func splitViewMasterPresentable(_ presentable: Presentable, wantsToStack detailPresentable: Presentable, popCompletion: (() -> Void)?)
    
    /// Reset detail stack with placeholder
    func splitViewMasterPresentableWantsToResetDetail(_ presentable: Presentable)
}

/// `SplitViewMasterPresentableDelegate` default implementation
extension SplitViewMasterPresentableDelegate {
    func splitViewMasterPresentable(_ presentable: Presentable, wantsToDisplay detailPresentable: Presentable) {
        splitViewMasterPresentable(presentable, wantsToReplaceDetailWith: detailPresentable, popCompletion: nil)
    }
}

/// Protocol used by the master view presentable of a UISplitViewController
protocol SplitViewMasterPresentable: AnyObject, Presentable {
        
    var splitViewMasterPresentableDelegate: SplitViewMasterPresentableDelegate? { get set }    
    
    /// Return the currently selected and visible NavigationRouter
    /// It will be used to manage detail controllers
    var selectedNavigationRouter: NavigationRouterType? { get }
}
