import Foundation
import UIKit

/**
 ObjC class for holding fonts for use in UIKit.
 */
@objcMembers public class FontsUIKit: NSObject, Fonts {
    
    public var largeTitle: UIFont
    
    public var largeTitleB: UIFont
    
    public var title1: UIFont
    
    public var title1B: UIFont
    
    public var title2: UIFont
    
    public var title2B: UIFont
    
    public var title3: UIFont
    
    public var title3SB: UIFont
    
    public var headline: UIFont
    
    public var subheadline: UIFont
    
    public var body: UIFont
    
    public var bodySB: UIFont
    
    public var callout: UIFont
    
    public var calloutSB: UIFont
    
    public var footnote: UIFont
    
    public var footnoteSB: UIFont
    
    public var caption1: UIFont
    
    public var caption1SB: UIFont
    
    public var caption2: UIFont
    
    public var caption2SB: UIFont
    
    public init(values: ElementFonts) {
        self.largeTitle = values.largeTitle
        self.largeTitleB = values.largeTitleB
        self.title1 = values.title1
        self.title1B = values.title1B
        self.title2 = values.title2
        self.title2B = values.title2B
        self.title3 = values.title3
        self.title3SB = values.title3SB
        self.headline = values.headline
        self.subheadline = values.subheadline
        self.body = values.body
        self.bodySB = values.bodySB
        self.callout = values.callout
        self.calloutSB = values.calloutSB
        self.footnote = values.footnote
        self.footnoteSB = values.footnoteSB
        self.caption1 = values.caption1
        self.caption1SB = values.caption1SB
        self.caption2 = values.caption2
        self.caption2SB = values.caption2SB
    }
}
