import Foundation

/// TemplateScreenViewController view state
enum TemplateScreenViewState {
    case idle
    case loading
    case loaded(_ displayName: String)
    case error(Error)
}
