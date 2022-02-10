import SwiftUI

@available(iOS 14.0, *)
struct UserSuggestionListWithInputViewModel {
    let listViewModel: UserSuggestionViewModelProtocol
    let callback: (String)->()
}

@available(iOS 14.0, *)
struct UserSuggestionListWithInput: View {
    
    // MARK: - Properties
    
    // MARK: Private
    
    // MARK: Public
    
    var viewModel: UserSuggestionListWithInputViewModel
    @State private var inputText: String = ""
    
    var body: some View {
        VStack(spacing: 0.0) {
            UserSuggestionList(viewModel: viewModel.listViewModel.context)
            TextField("Search for user", text: $inputText)
                .background(Color.white)
                .onChange(of: inputText, perform:viewModel.callback)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding([.leading, .trailing])
                .onAppear {
                    inputText = "@-" // Make the list show all available mock results
                }
        }
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct UserSuggestionListWithInput_Previews: PreviewProvider {
    static let stateRenderer = MockUserSuggestionScreenState.stateRenderer
    static var previews: some View {
        stateRenderer.screenGroup()
    }
}
