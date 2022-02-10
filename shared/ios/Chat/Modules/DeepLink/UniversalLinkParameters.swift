import Foundation

/// Parameters describing a universal link
@objcMembers
class UniversalLinkParameters: NSObject {
        
    // MARK: - Properties
        
    /// The unprocessed the universal link URL
    let universalLinkURL: URL
    
    /// The fragment part of the universal link
    let fragment: String
        
    /// Presentation parameters
    let presentationParameters: ScreenPresentationParameters
    
    // MARK: - Setup
    
    init(fragment: String,
         universalLinkURL: URL,
         presentationParameters: ScreenPresentationParameters) {
        self.fragment = fragment
        self.universalLinkURL = universalLinkURL
        self.presentationParameters = presentationParameters
        
        super.init()
    }
    
    convenience init?(universalLinkURL: URL,
                      presentationParameters: ScreenPresentationParameters) {
        
        guard let fixedURL = Tools.fixURL(withSeveralHashKeys: universalLinkURL), let fragment = fixedURL.fragment else {
            return nil
        }
        
        self.init(fragment: fragment, universalLinkURL: universalLinkURL, presentationParameters: presentationParameters)
    }
}
