import Foundation

final class CameraAccessAlertPresenter {
        
    // MARK: - Public
    
    func presentPermissionDeniedAlert(from presentingViewController: UIViewController, animated: Bool) {
        guard let settingsURL = URL(string: UIApplication.openSettingsURLString) else {
            return
        }
        
        let appDisplayName = Bundle.main.infoDictionary?["CFBundleDisplayName"] as? String ?? ""
        
        let alert = UIAlertController(title: VectorL10n.camera, message: VectorL10n.cameraAccessNotGranted(appDisplayName), preferredStyle: .alert)
        
        let cancelActionTitle = MatrixKitL10n.ok
        let cancelAction = UIAlertAction(title: cancelActionTitle, style: .cancel)
        
        let settingsActionTitle = MatrixKitL10n.settings
        let settingsAction = UIAlertAction(title: settingsActionTitle, style: .default, handler: { _ in
            UIApplication.shared.open(settingsURL, options: [:], completionHandler: { (succeed) in
                if !succeed {
                    MXLog.debug("[CameraPresenter] Fails to open settings")
                }
            })
        })
        
        alert.addAction(cancelAction)
        alert.addAction(settingsAction)
        
        presentingViewController.present(alert, animated: animated, completion: nil)
    }
    
    func presentCameraUnavailableAlert(from presentingViewController: UIViewController, animated: Bool) {
        
        let alert = UIAlertController(title: VectorL10n.camera, message: VectorL10n.cameraUnavailable, preferredStyle: .alert)
        
        let okAction = UIAlertAction(title: VectorL10n.accept, style: .default, handler: nil)
        
        alert.addAction(okAction)
        
        presentingViewController.present(alert, animated: true, completion: nil)
    }
}
