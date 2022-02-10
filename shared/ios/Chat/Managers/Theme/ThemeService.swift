import Foundation

extension ThemeService {
    
    var themeIdentifier: ThemeIdentifier? {
        guard let themeId = self.themeId else {
            return nil
        }        
        return ThemeIdentifier(rawValue: themeId)
    }    
}
