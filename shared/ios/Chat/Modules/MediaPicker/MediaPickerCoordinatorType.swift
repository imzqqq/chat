// File created from FlowTemplate
// $ createRootCoordinator.sh Test MediaPicker

import Foundation

protocol MediaPickerCoordinatorDelegate: AnyObject {
    func mediaPickerCoordinator(_ coordinator: MediaPickerCoordinatorType, didSelectImageData imageData: Data, withUTI uti: MXKUTI?)
    func mediaPickerCoordinator(_ coordinator: MediaPickerCoordinatorType, didSelectVideo videoAsset: AVAsset)
    func mediaPickerCoordinator(_ coordinator: MediaPickerCoordinatorType, didSelectAssets assets: [PHAsset])
    func mediaPickerCoordinatorDidCancel(_ coordinator: MediaPickerCoordinatorType)
}

/// `MediaPickerCoordinatorType` is a protocol describing a Coordinator that handle keybackup setup navigation flow.
protocol MediaPickerCoordinatorType: Coordinator, Presentable {
    var delegate: MediaPickerCoordinatorDelegate? { get }
}
