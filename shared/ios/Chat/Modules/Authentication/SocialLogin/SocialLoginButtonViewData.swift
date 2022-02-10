import Foundation

/// SocialLoginButton view data
struct SocialLoginButtonViewData {
    
    /// Identify provider identifier
    let identifier: String
    
    /// Button title
    let title: String
    
    /// Default button style
    let defaultStyle: SocialLoginButtonStyle
    
    /// Button style per theme identifier
    let themeStyles: [String: SocialLoginButtonStyle]
}
