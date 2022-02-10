import Foundation

class CallVCExitPipOperation: AsyncOperation {
    
    private var presenter: CallPresenter
    private var callVC: UIViewController
    private var completion: (() -> Void)?
    
    init(presenter: CallPresenter,
         callVC: UIViewController,
         completion: (() -> Void)? = nil) {
        self.presenter = presenter
        self.callVC = callVC
        self.completion = completion
    }
    
    override func main() {
        presenter.delegate?.callPresenter(presenter, exitPipForCallViewController: callVC, completion: {
            self.finish()
            self.completion?()
        })
    }
    
}
