import Foundation
import SwiftUI

/**
 Struct for holding fonts for use in SwiftUI.
 */
@available(iOS 14.0, *)
public struct FontSwiftUI: Fonts {
    
    public let uiFonts: ElementFonts
    
    public var largeTitle: Font
    
    public var largeTitleB: Font
    
    public var title1: Font
    
    public var title1B: Font
    
    public var title2: Font
    
    public var title2B: Font
    
    public var title3: Font
    
    public var title3SB: Font
    
    public var headline: Font
    
    public var subheadline: Font
    
    public var body: Font
    
    public var bodySB: Font
    
    public var callout: Font
    
    public var calloutSB: Font
    
    public var footnote: Font
    
    public var footnoteSB: Font
    
    public var caption1: Font
    
    public var caption1SB: Font
    
    public var caption2: Font
    
    public var caption2SB: Font
    
    public init(values: ElementFonts) {
        self.uiFonts = values
        
        self.largeTitle = Font(values.largeTitle)
        self.largeTitleB = Font(values.largeTitleB)
        self.title1 = Font(values.title1)
        self.title1B = Font(values.title1B)
        self.title2 = Font(values.title2)
        self.title2B = Font(values.title2B)
        self.title3 = Font(values.title3)
        self.title3SB = Font(values.title3SB)
        self.headline = Font(values.headline)
        self.subheadline = Font(values.subheadline)
        self.body = Font(values.body)
        self.bodySB = Font(values.bodySB)
        self.callout = Font(values.callout)
        self.calloutSB = Font(values.calloutSB)
        self.footnote = Font(values.footnote)
        self.footnoteSB = Font(values.footnoteSB)
        self.caption1 = Font(values.caption1)
        self.caption1SB = Font(values.caption1SB)
        self.caption2 = Font(values.caption2)
        self.caption2SB = Font(values.caption2SB)
    }
}
