import Foundation
import DesignKit

@available(iOS 14.0, *)
struct DarkThemeSwiftUI: ThemeSwiftUI {
    var identifier: ThemeIdentifier = .dark
    var colors: ColorSwiftUI = DarkColors.swiftUI
    var fonts: FontSwiftUI = FontSwiftUI(values: ElementFonts())
}
