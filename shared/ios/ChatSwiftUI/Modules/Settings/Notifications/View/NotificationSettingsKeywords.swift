import SwiftUI

/// Renders the keywords input, driven by 'NotificationSettingsViewModel'.
@available(iOS 14.0, *)
struct NotificationSettingsKeywords: View {
    @ObservedObject var viewModel: NotificationSettingsViewModel
    var body: some View {
        ChipsInput(
            titles: viewModel.viewState.keywords,
            didAddChip: viewModel.add(keyword:),
            didDeleteChip: viewModel.remove(keyword:),
            placeholder: VectorL10n.settingsNewKeyword
        )
        .disabled(!(viewModel.viewState.selectionState[.keywords] ?? false))

    }
}

@available(iOS 14.0, *)
struct Keywords_Previews: PreviewProvider {
    static let viewModel = NotificationSettingsViewModel(
        notificationSettingsService: MockNotificationSettingsService.example,
        ruleIds: NotificationSettingsScreen.mentionsAndKeywords.pushRules
    )
    static var previews: some View {
        NotificationSettingsKeywords(viewModel: viewModel)
    }
}
