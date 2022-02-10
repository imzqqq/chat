import Foundation

/// Space list sections
enum SpaceListSection {
    case home(_ viewData: SpaceListItemViewData)
    case spaces(_ viewDataList: [SpaceListItemViewData])
}
