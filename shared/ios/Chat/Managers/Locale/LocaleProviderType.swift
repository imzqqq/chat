import Foundation

/**
 Used to provide an application/target specific locale.
 */
protocol LocaleProviderType {
    static var locale: Locale? { get }
}
