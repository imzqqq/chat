import Foundation
import SwiftUI

@available(iOS 14.0, *)
enum MockPollEditFormScreenState: MockScreenState, CaseIterable {
    case standard
    
    var screenType: Any.Type {
        MockPollEditFormScreenState.self
    }
    
    var screenView: ([Any], AnyView)  {
        let viewModel = PollEditFormViewModel()
        return ([viewModel], AnyView(PollEditForm(viewModel: viewModel.context)))
    }
}
