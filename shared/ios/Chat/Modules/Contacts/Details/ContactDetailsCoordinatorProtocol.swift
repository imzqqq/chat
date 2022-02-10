// File created from ScreenTemplate
// $ createScreen.sh Contacts ContactDetails

import Foundation

protocol ContactDetailsCoordinatorDelegate: AnyObject {
    func contactDetailsCoordinatorDidCancel(_ coordinator: ContactDetailsCoordinatorProtocol)
}

/// `ContactDetailsCoordinatorProtocol` is a protocol describing a Coordinator that handle contact details navigation flow.
protocol ContactDetailsCoordinatorProtocol: Coordinator, Presentable {
    var delegate: ContactDetailsCoordinatorDelegate? { get }
}
