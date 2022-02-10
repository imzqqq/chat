import UIKit

/// Fonts at  https://www.figma.com/file/X4XTH9iS2KGJ2wFKDqkyed/Compound?node-id=1362%3A0
@objcMembers
public class ElementFonts {
    
    // MARK: - Setup
    
    public init() {
    }
    
    // MARK: - Private
    
    /// Returns an instance of the font associated with the text style and scaled appropriately for the content size category defined in the trait collection.
    /// Keep this method private method at the moment and create a DesignKit.Fonts.TextStyle if needed.
    fileprivate func font(forTextStyle textStyle: UIFont.TextStyle, compatibleWith traitCollection: UITraitCollection? = nil) -> UIFont {
        return UIFont.preferredFont(forTextStyle: textStyle, compatibleWith: traitCollection)
    }
}

// MARK: - Fonts protocol
extension ElementFonts: Fonts {    
    
    public var largeTitle: UIFont {
        return self.font(forTextStyle: .largeTitle)
    }
    
    public var largeTitleB: UIFont {
        return self.largeTitle.vc_bold
    }
            
    public var title1: UIFont {
        return self.font(forTextStyle: .title1)
    }
    
    public var title1B: UIFont {
        return self.title1.vc_bold
    }
    
    public var title2: UIFont {
        return self.font(forTextStyle: .title2)
    }
    
    public var title2B: UIFont {
        return self.title2.vc_bold
    }
    
    public var title3: UIFont {
        return self.font(forTextStyle: .title3)
    }
    
    public var title3SB: UIFont {
        return self.title3.vc_semiBold
    }
    
    public var headline: UIFont {
        return self.font(forTextStyle: .headline)
    }
    
    public var subheadline: UIFont {
        return self.font(forTextStyle: .subheadline)
    }
    
    public var body: UIFont {
        return self.font(forTextStyle: .body)
    }
    
    public var bodySB: UIFont {
        return self.body.vc_semiBold
    }
    
    public var callout: UIFont {
        return self.font(forTextStyle: .callout)
    }
    
    public var calloutSB: UIFont {
        return self.callout.vc_semiBold
    }
    
    public var footnote: UIFont {
        return self.font(forTextStyle: .footnote)
    }
    
    public var footnoteSB: UIFont {
        return self.footnote.vc_semiBold
    }
    
    public var caption1: UIFont {
        return self.font(forTextStyle: .caption1)
    }
    
    public var caption1SB: UIFont {
        return self.caption1.vc_semiBold
    }
    
    public var caption2: UIFont {
        return self.font(forTextStyle: .caption2)
    }
    
    public var caption2SB: UIFont {
        return self.caption2.vc_semiBold
    }
}
