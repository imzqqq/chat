import Foundation

@objc
protocol CallPresenterDelegate: AnyObject {
    //  Call screens
    func callPresenter(_ presenter: CallPresenter,
                       presentCallViewController viewController: UIViewController,
                       completion:(() -> Void)?)
    func callPresenter(_ presenter: CallPresenter,
                       dismissCallViewController viewController: UIViewController,
                       completion:(() -> Void)?)
    
    //  PiP
    func callPresenter(_ presenter: CallPresenter,
                       enterPipForCallViewController viewController: UIViewController,
                       completion:(() -> Void)?)
    
    func callPresenter(_ presenter: CallPresenter,
                       exitPipForCallViewController viewController: UIViewController,
                       completion:(() -> Void)?)
}
