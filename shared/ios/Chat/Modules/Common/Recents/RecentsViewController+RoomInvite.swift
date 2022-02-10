import Foundation

extension RecentsViewController {
    
    @objc
    func canShowRoomPreview(for summary: MXRoomSummaryProtocol) -> Bool {
        let membershipTransitionState = summary.membershipTransitionState
        
        // NOTE: For the moment do not offer the possibility to show room preview when invitation action is in progress
        
        switch membershipTransitionState {
        case .failedJoining, .failedLeaving:
            return false
        default:
            return true
        }
    }
}
