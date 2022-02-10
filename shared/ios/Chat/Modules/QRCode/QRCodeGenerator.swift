import Foundation

final class QRCodeGenerator {
    
    // MARK: - Constants
    
    private enum Constants {
        static let qrCodeGeneratorFilter = "CIQRCodeGenerator"
        static let qrCodeInputCorrectionLevel = "M"
    }
    
    // MARK: - Public
    
    func generateCode(from data: Data, with size: CGSize) -> UIImage? {
        guard let filter = CIFilter(name: Constants.qrCodeGeneratorFilter) else {
            return nil
        }
        
        filter.setValue(data, forKey: "inputMessage")
        filter.setValue(Constants.qrCodeInputCorrectionLevel, forKey: "inputCorrectionLevel") // Be sure to use same error resilience level as other platform
        
        guard let ciImage = filter.outputImage else {
            return nil
        }
        
        let scaleX = size.width/ciImage.extent.size.width
        let scaleY = size.height/ciImage.extent.size.height
        
        let transform = CGAffineTransform(scaleX: scaleX, y: scaleY)
            
        let transformedCIImage = ciImage.transformed(by: transform)
        return UIImage(ciImage: transformedCIImage)
    }
}
