import Foundation

/// UserSessionProtocol represents a user session regardless of the communication protocol
protocol UserSessionProtocol {
    var userId: String { get }
}

/// UserSession represents a Matrix user session
/// Note: UserSessionProtocol can be renamed UserSession and UserSession -> MatrixUserSession if we keep this abstraction.
@objcMembers
class UserSession: NSObject, UserSessionProtocol {
        
    // MARK: - Properties
    
    // MARK: Public
    
    let account: MXKAccount
    // Keep strong reference to the MXSession because account.mxSession can become nil on logout or failure
    let matrixSession: MXSession
    
    var userId: String {
        guard let userId = self.account.mxCredentials.userId else {
            fatalError("[UserSession] identifier: account.mxCredentials.userId should be defined")
        }
        return userId
    }
    
    // MARK: - Setup
    
    init(account: MXKAccount, matrixSession: MXSession) {
        self.account = account
        self.matrixSession = matrixSession
        super.init()
    }
}
