import SwiftUI

@available(iOS 14.0, *)
struct TemplateUserProfile: View {

    // MARK: - Properties
    
    // MARK: Private
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    
    @ObservedObject var viewModel: TemplateUserProfileViewModel.Context
    
    var body: some View {
        EmptyView()
        VStack {
            TemplateUserProfileHeader(
                avatar: viewModel.viewState.avatar,
                displayName: viewModel.viewState.displayName,
                presence: viewModel.viewState.presence
            )
            Divider()
            HStack{
                Text("Counter: \(viewModel.viewState.count)")
                    .font(theme.fonts.title2)
                    .foregroundColor(theme.colors.secondaryContent)
                Button("-") {
                    viewModel.send(viewAction: .decrementCount)
                }
                Button("+") {
                    viewModel.send(viewAction: .incrementCount)
                }
            }
            .frame(maxHeight: .infinity)
        }
        .background(theme.colors.background)
        .frame(maxHeight: .infinity)
        .navigationTitle(viewModel.viewState.displayName ?? "")
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                Button(VectorL10n.done) {
                    viewModel.send(viewAction: .done)
                }
            }
            ToolbarItem(placement: .cancellationAction) {
                Button(VectorL10n.cancel) {
                    viewModel.send(viewAction: .cancel)
                }
            }
        }
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct TemplateUserProfile_Previews: PreviewProvider {
    static let stateRenderer = MockTemplateUserProfileScreenState.stateRenderer
    static var previews: some View {
        stateRenderer.screenGroup()
    }
}
