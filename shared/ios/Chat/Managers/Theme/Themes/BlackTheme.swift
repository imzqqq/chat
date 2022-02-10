import UIKit

class BlackTheme: DarkTheme {

    override init() {
        super.init()
        self.identifier = ThemeIdentifier.black.rawValue
        self.backgroundColor = UIColor(rgb: 0x000000)
        self.baseColor = UIColor(rgb: 0x000000)
        self.headerBackgroundColor = UIColor(rgb: 0x000000)
        self.headerBorderColor = UIColor(rgb: 0x15191E)
    }
}
