import Foundation
import DesignKit

@available(iOS 14.0, *)
struct DefaultThemeSwiftUI: ThemeSwiftUI {
    var identifier: ThemeIdentifier = .light
    var colors: ColorSwiftUI = LightColors.swiftUI
    var fonts: FontSwiftUI = FontSwiftUI(values: ElementFonts())
}
