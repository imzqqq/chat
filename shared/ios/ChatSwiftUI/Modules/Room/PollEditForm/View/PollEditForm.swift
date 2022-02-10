import SwiftUI

@available(iOS 14.0, *)
struct PollEditForm: View {
    
    // MARK: - Properties
    
    // MARK: Private
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    
    @ObservedObject var viewModel: PollEditFormViewModel.Context
    
    var body: some View {
        NavigationView {
            GeometryReader { proxy in
                ScrollView {
                    VStack(alignment: .leading, spacing: 32.0) {
                        
                        VStack(alignment: .leading, spacing: 16.0) {
                            Text(VectorL10n.pollEditFormPollQuestionOrTopic)
                                .font(theme.fonts.title3SB)
                                .foregroundColor(theme.colors.primaryContent)
                            
                            VStack(alignment: .leading, spacing: 8.0) {
                                Text(VectorL10n.pollEditFormQuestionOrTopic)
                                    .font(theme.fonts.subheadline)
                                    .foregroundColor(theme.colors.primaryContent)
                                
                                MultilineTextField(VectorL10n.pollEditFormInputPlaceholder, text: $viewModel.question.text)
                            }
                        }
                        
                        VStack(alignment: .leading, spacing: 16.0) {
                            Text(VectorL10n.pollEditFormCreateOptions)
                                .font(theme.fonts.title3SB)
                                .foregroundColor(theme.colors.primaryContent)
                            
                            ForEach(0..<viewModel.answerOptions.count, id: \.self) { index in
                                SafeBindingCollectionEnumerator($viewModel.answerOptions, index: index) { binding in
                                    AnswerOptionGroup(text: binding.text, index: index) {
                                        viewModel.send(viewAction: .deleteAnswerOption(viewModel.answerOptions[index]))
                                    }
                                }
                            }
                        }
                        
                        Button(VectorL10n.pollEditFormAddOption) {
                            viewModel.send(viewAction: .addAnswerOption)
                        }
                        .disabled(!viewModel.viewState.addAnswerOptionButtonEnabled)
                        
                        Spacer()
                        
                        Button(VectorL10n.pollEditFormCreatePoll) {
                            viewModel.send(viewAction: .create)
                        }
                        .buttonStyle(PrimaryActionButtonStyle(enabled: viewModel.viewState.confirmationButtonEnabled))
                        .disabled(!viewModel.viewState.confirmationButtonEnabled)
                    }
                    .animation(.easeInOut(duration: 0.2))
                    .padding()
                    .frame(minHeight: proxy.size.height) // Make the VStack fill the ScrollView's parent
                    .toolbar {
                        ToolbarItem(placement: .navigationBarLeading) {
                            Button(VectorL10n.cancel, action: {
                                viewModel.send(viewAction: .cancel)
                            })
                        }
                        ToolbarItem(placement: .principal) {
                            Text(VectorL10n.pollEditFormCreatePoll)
                                .font(.headline)
                                .foregroundColor(theme.colors.primaryContent)
                        }
                    }
                    .navigationBarTitleDisplayMode(.inline)
                }
            }
        }
        .accentColor(theme.colors.accent)
    }
}

@available(iOS 14.0, *)
private struct AnswerOptionGroup: View {
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    @State private var focused = false
    
    @Binding var text: String
    
    let index: Int
    let onDelete: () -> Void
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8.0) {
            Text(VectorL10n.pollEditFormOptionNumber(index + 1))
                .font(theme.fonts.subheadline)
                .foregroundColor(theme.colors.primaryContent)
            
            HStack(spacing: 16.0) {
                TextField(VectorL10n.pollEditFormInputPlaceholder, text: $text, onEditingChanged: { edit in
                    self.focused = edit
                })
                .textFieldStyle(BorderedInputFieldStyle(theme: _theme, isEditing: focused))
                Button {
                    onDelete()
                } label: {
                    Image(uiImage:Asset.Images.pollDeleteOptionIcon.image)
                }
                .accessibilityIdentifier("Delete answer option")
            }
        }
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct PollEditForm_Previews: PreviewProvider {
    static let stateRenderer = MockPollEditFormScreenState.stateRenderer
    static var previews: some View {
        stateRenderer.screenGroup()
    }
}
