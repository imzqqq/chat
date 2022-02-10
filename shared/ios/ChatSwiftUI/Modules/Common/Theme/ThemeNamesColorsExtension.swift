import Foundation
import SwiftUI

@available(iOS 14.0, *)
extension ThemeSwiftUI {
    
    /// Get the stable display name color based on userId.
    /// - Parameter userId: The user id used to hash.
    /// - Returns: The SwiftUI color for the associated userId.
    func displayNameColor(for userId: String) -> Color {
        let senderNameColorIndex = Int(userId.vc_hashCode % Int32(colors.namesAndAvatars.count))
        return colors.namesAndAvatars[senderNameColorIndex]
    }
}
