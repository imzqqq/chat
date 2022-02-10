import Foundation

/// Colors at https://www.figma.com/file/X4XTH9iS2KGJ2wFKDqkyed/Compound?node-id=1255%3A1104
public protocol Colors {
    
    associatedtype ColorType
    
    /// - Focused/Active states
    /// - CTAs
    var accent: ColorType { get }
    
    /// - Error messages
    /// - Content requiring user attention
    /// - Notification, alerts
    var alert: ColorType { get }
    
    /// - Text
    /// - Icons
    var primaryContent: ColorType { get }
    
    /// - Text
    /// - Icons
    var secondaryContent: ColorType { get }
    
    /// - Text
    /// - Icons
    var tertiaryContent: ColorType { get }
    
    /// - Text
    /// - Icons
    var quarterlyContent: ColorType { get }
    
    /// - separating lines and other UI components
    var quinaryContent: ColorType { get }
    
    /// - System-based areas and backgrounds
    var system: ColorType { get }
    
    /// Separating line
    var separator: ColorType { get }
    
    //  Cards, tiles
    var tile: ColorType { get }
    
    /// Top navigation background on iOS
    var navigation: ColorType { get }
    
    /// Background UI color
    var background: ColorType { get }
    
    /// - Names in chat timeline
    /// - Avatars default states that include first name letter
    var namesAndAvatars: [ColorType] { get }
    
}
