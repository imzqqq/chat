import Foundation

/// CameraAccessManager handles camera availability and authorization.
final class CameraAccessManager {
    
    // MARK: - Properties
    
    var isCameraAvailable: Bool {
        return UIImagePickerController.isSourceTypeAvailable(.camera)
    }
    
    var isCameraAccessGranted: Bool {
        return AVCaptureDevice.authorizationStatus(for: .video) == .authorized
    }
    
    // MARK: - Public
        
    func askAndRequestCameraAccessIfNeeded(completion: @escaping (_ granted: Bool) -> Void) {
        
        let authorizationStatus = AVCaptureDevice.authorizationStatus(for: .video)
        
        switch authorizationStatus {
        case .authorized:
            completion(true)
        case .notDetermined:
            self.requestCameraAccess(completion: { (granted) in
                completion(granted)
            })
        case .denied, .restricted:
            completion(false)
        @unknown default:
            break
        }
    }
    
    // MARK: - Private
    
    private func requestCameraAccess(completion: @escaping (_ granted: Bool) -> Void) {
        AVCaptureDevice.requestAccess(for: .video) { granted in
            DispatchQueue.main.async {
                completion(granted)
            }
        }
    }
}
