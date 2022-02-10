import Foundation
import SwiftUI

@available(iOS 14.0, *)
struct ScreenStateInfo {
    var dependencies: [Any]
    var view: AnyView
    var stateTitle: String
    var fullScreenTitle: String
    var stateKey: String
}
