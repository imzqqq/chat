import Foundation

/// `OnBoardingManager` is used to manage onboarding steps, like create DM room with riot bot.
final public class OnBoardingManager: NSObject {
    
    // MARK: - Constants
    
    private enum Constants {
        static let riotBotMatrixId = "@riot-bot:chat.imzqqq.top"
        static let createChatBotDMRequestMaxNumberOfTries: UInt = UInt.max
    }
    
    // MARK: - Properties
    
    private let session: MXSession
    
    // MARK: - Setup & Teardown
    
    @objc public init(session: MXSession) {
        self.session = session
        
        super.init()
    }
    
    // MARK: - Public
    
    @objc public func createChatBotDirectMessageIfNeeded(success: (() -> Void)?, failure: ((Error) -> Void)?) {
        
        // Check user has joined no rooms so is a new comer
        guard self.isUserJoinedARoom() == false else {
            // As the user has already rooms, one of their riot client has already created a room with riot bot
            success?()
            return
        }

        // Check first that the user homeserver is federated with the  Chat-bot homeserver
        self.session.matrixRestClient.avatarUrl(forUser: Constants.riotBotMatrixId) { (response) in

            switch response {
            case .success:

                // Create DM room with Chat-bot
                let roomCreationParameters = MXRoomCreationParameters(forDirectRoomWithUser: Constants.riotBotMatrixId)
                let httpOperation = self.session.createRoom(parameters: roomCreationParameters) { (response) in

                    switch response {
                    case .success:
                        success?()
                    case .failure(let error):
                        MXLog.debug("[OnBoardingManager] Create chat with riot-bot failed")
                        failure?(error)
                    }
                }

                // Make multipe tries, until we get a response
                httpOperation.maxNumberOfTries = Constants.createChatBotDMRequestMaxNumberOfTries

            case .failure(let error):
                MXLog.debug("[OnBoardingManager] riot-bot is unknown or the user hs is non federated. Do not try to create a room with riot-bot")
                failure?(error)
            }
        }
    }
    
    // MARK: - Private
    
    private func isUserJoinedARoom() -> Bool {
        guard let roomSummaries = self.session.roomsSummaries() else {
            return false
        }
        
        var isUSerJoinedARoom = false
        
        for roomSummary in roomSummaries {
            if case .join = roomSummary.membership {
                isUSerJoinedARoom = true
                break
            }
        }

        return isUSerJoinedARoom
    }
}
