// File created from ScreenTemplate
// $ createScreen.sh Start UserVerificationStart

import Foundation

protocol UserVerificationStartCoordinatorDelegate: AnyObject {
    
    func userVerificationStartCoordinator(_ coordinator: UserVerificationStartCoordinatorType, otherDidAcceptRequest request: MXKeyVerificationRequest)        
    
    func userVerificationStartCoordinatorDidCancel(_ coordinator: UserVerificationStartCoordinatorType)
}

/// `UserVerificationStartCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol UserVerificationStartCoordinatorType: Coordinator, Presentable {
    var delegate: UserVerificationStartCoordinatorDelegate? { get }
}
