import Foundation

/**
 Provides the locale logic for Chat app based on mx languages.
 */
class LocaleProvider: LocaleProviderType {
    static var locale: Locale? {
        if let localeIdentifier = Bundle.mxk_language() {
           return Locale(identifier: localeIdentifier)
        } else if let fallbackLocaleIdentifier = Bundle.mxk_fallbackLanguage() {
           return Locale(identifier: fallbackLocaleIdentifier)
        }
        return nil
    }
}
