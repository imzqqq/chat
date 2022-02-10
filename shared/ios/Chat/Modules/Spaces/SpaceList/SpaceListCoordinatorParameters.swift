import Foundation

class SpaceListCoordinatorParameters {
    let userSessionsService: UserSessionsService
    
    init(userSessionsService: UserSessionsService) {
        self.userSessionsService = userSessionsService
    }
}
