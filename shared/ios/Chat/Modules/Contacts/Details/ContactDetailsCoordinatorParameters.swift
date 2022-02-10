// File created from ScreenTemplate
// $ createScreen.sh Contacts ContactDetails

import Foundation

/// ContactDetailsCoordinator input parameters
struct ContactDetailsCoordinatorParameters {
    
    /// The displayed contact
    let contact: MXKContact
    
    /// Enable voip call (voice/video). NO by default
    let enableVoipCall: Bool
    
    init(contact: MXKContact,
         enableVoipCall: Bool = false) {
        self.contact = contact
        self.enableVoipCall = enableVoipCall
    }
}
