import Foundation
import DesignKit

@available(iOS 14.0, *)
protocol ThemeSwiftUI: ThemeSwiftUIType {
    var identifier: ThemeIdentifier { get }
}
