// File created from ScreenTemplate
// $ createScreen.sh Communities GroupDetails

import Foundation

/// GroupDetailsCoordinator input parameters
struct GroupDetailsCoordinatorParameters {
    
    /// The Matrix session
    let session: MXSession
    
    /// The group for which the details are displayed
    let group: MXGroup
}
