// File created from ScreenTemplate
// $ createScreen.sh SetPinCode/SetupBiometrics SetupBiometrics

import Foundation

protocol SetupBiometricsCoordinatorDelegate: AnyObject {
    func setupBiometricsCoordinatorDidComplete(_ coordinator: SetupBiometricsCoordinatorType)
    func setupBiometricsCoordinatorDidCompleteWithReset(_ coordinator: SetupBiometricsCoordinatorType, dueToTooManyErrors: Bool)
    func setupBiometricsCoordinatorDidCancel(_ coordinator: SetupBiometricsCoordinatorType)
}

/// `SetupBiometricsCoordinatorType` is a protocol describing a Coordinator that handle key backup setup passphrase navigation flow.
protocol SetupBiometricsCoordinatorType: Coordinator, Presentable {
    var delegate: SetupBiometricsCoordinatorDelegate? { get }
}
