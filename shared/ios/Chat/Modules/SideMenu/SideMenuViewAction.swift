// File created from ScreenTemplate
// $ createScreen.sh SideMenu SideMenu

import Foundation

/// SideMenuViewController view actions exposed to view model
enum SideMenuViewAction {
    case loadData
    case tap(menuItem: SideMenuItem, sourceView: UIView)
}
