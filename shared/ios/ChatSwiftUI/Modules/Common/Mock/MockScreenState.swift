import SwiftUI

/// Used for mocking top level screens and their various states.
@available(iOS 14.0, *)
protocol MockScreenState {
    static var screenStates: [MockScreenState] { get }
    var screenType: Any.Type { get }
    var screenView: ([Any], AnyView) { get }
    var stateTitle: String { get }
}

@available(iOS 14.0, *)
extension MockScreenState {
    
    /// Get a list of the screens for every screen state.
    static var stateRenderer: StateRenderer {
        let depsAndViews  = screenStates.map(\.screenView)
        let deps = depsAndViews.map({ $0.0 })
        let views = depsAndViews.map({ $0.1 })
        let stateTitles = screenStates.map(\.stateTitle)
        let fullScreenTitles = screenStates.map(\.fullScreenTitle)
        
        var states = [ScreenStateInfo]()
        for i in 0..<deps.count {
            let dep = deps[i]
            let view = views[i]
            let stateTitle = stateTitles[i]
            let stateKey = screenStateKeys[i]
            let fullScreenTitle = fullScreenTitles[i]
            states.append(ScreenStateInfo(dependencies: dep, view: view, stateTitle: stateTitle, fullScreenTitle:fullScreenTitle, stateKey: stateKey))
        }
        
        return StateRenderer(states: states)
    }
    
    /// A unique key to identify each screen state.
    static var screenStateKeys: [String] {
        return screenStates.enumerated().map { (index, state) in
            state.screenName + String(index)
        }
    }
    
    /// A title to represent the screen and it's screen state
    var screenName: String {
        "\(String(describing: screenType.self))"
    }
    
    /// A title to represent this screen state
    var stateTitle: String {
        String(describing: self)
    }
    
    /// A title to represent the screen and it's screen state
    var fullScreenTitle: String {
        "\(screenName): \(stateTitle)"
    }

}

@available(iOS 14.0, *)
extension MockScreenState where Self: CaseIterable {
    static var screenStates: [MockScreenState] {
        return Array(self.allCases)
    }
}
