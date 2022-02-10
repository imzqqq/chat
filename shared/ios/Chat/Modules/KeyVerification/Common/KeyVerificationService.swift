import Foundation

final class KeyVerificationService {
    
    private let cameraAccessManager: CameraAccessManager
        
    private var supportSetupKeyVerificationByUser: [String: Bool] = [:] // Cached server response
    
    init() {
        self.cameraAccessManager = CameraAccessManager()
    }
    
    func supportedKeyVerificationMethods() -> [String] {
        var supportedMethods: [String] = [
            MXKeyVerificationMethodSAS,
            MXKeyVerificationMethodQRCodeShow,
            MXKeyVerificationMethodReciprocate
        ]
        
        if self.cameraAccessManager.isCameraAvailable {
            supportedMethods.append(MXKeyVerificationMethodQRCodeScan)
        }
        
        return supportedMethods
    }
}
