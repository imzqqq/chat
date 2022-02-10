import SwiftUI

@available(iOS 14.0, *)
struct ScreenList: View {
    
    private var allStates: [ScreenStateInfo]
    
    init(screens: [MockScreenState.Type]) {
        allStates = screens
            .map({ $0.stateRenderer })
            .flatMap{( $0.states )}
    }
    
    var body: some View {
        NavigationView {
            List {
                ForEach(0..<allStates.count) { i in
                    let state = allStates[i]
                    NavigationLink(destination: state.view) {
                        Text(state.fullScreenTitle)
                            .accessibilityIdentifier(state.stateKey)
                    }
                }
            }
        }
        .navigationTitle("Screen States")
    }
}

@available(iOS 14.0, *)
struct ScreenList_Previews: PreviewProvider {
    static var previews: some View {
        ScreenList(screens: [MockTemplateUserProfileScreenState.self])
    }
}
