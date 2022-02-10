import Foundation
import UIKit
import SwiftUI


/// Light theme colors.
public class LightColors {
    private static let values = ColorValues(
        accent: UIColor(rgb:0x0DBD8B),
        alert: UIColor(rgb:0xFF4B55),
        primaryContent: UIColor(rgb:0x17191C),
        secondaryContent: UIColor(rgb:0x737D8C),
        tertiaryContent: UIColor(rgb:0x8D97A5),
        quarterlyContent: UIColor(rgb:0xC1C6CD),
        quinaryContent: UIColor(rgb:0xE3E8F0),
        separator: UIColor(rgb:0xE3E8F0),
        system: UIColor(rgb:0xF4F6FA),
        tile: UIColor(rgb:0xF3F8FD),
        navigation: UIColor(rgb:0xF4F6FA),
        background: UIColor(rgb:0xFFFFFF),
        namesAndAvatars: [
            UIColor(rgb:0x368BD6),
            UIColor(rgb:0xAC3BA8),
            UIColor(rgb:0x03B381),
            UIColor(rgb:0xE64F7A),
            UIColor(rgb:0xFF812D),
            UIColor(rgb:0x2DC2C5),
            UIColor(rgb:0x5C56F5),
            UIColor(rgb:0x74D12C)
        ]
    )
    
    public static var uiKit = ColorsUIKit(values: values)
    @available(iOS 14.0, *)
    public static var swiftUI = ColorSwiftUI(values: values)
}





