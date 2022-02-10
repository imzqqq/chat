import SwiftUI

@available(iOS 14.0, *)
@main
/// ChatSwiftUI screens rendered for UI Tests.
struct ChatSwiftUIApp: App {
    init() {
        UILog.configure(logger: PrintLogger.self)
        
        switch UITraitCollection.current.userInterfaceStyle {
        case .dark:
            ThemePublisher.configure(themeId: .dark)
        default:
            ThemePublisher.configure(themeId: .light)
        }
        
    }
    var body: some Scene {
        WindowGroup {
            ScreenList(screens: MockAppScreens.appScreens)
        }
    }
}
