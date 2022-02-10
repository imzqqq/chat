// File created from ScreenTemplate
// $ createScreen.sh SideMenu SideMenu

import Foundation

struct SideMenuViewData {
    let userAvatarViewData: UserAvatarViewData
    let sideMenuItems: [SideMenuItem]
    let appVersion: String?
}

/// SideMenuViewController view state
enum SideMenuViewState {
    case loading
    case loaded(_ viewData: SideMenuViewData)
    case error(Error)
}
