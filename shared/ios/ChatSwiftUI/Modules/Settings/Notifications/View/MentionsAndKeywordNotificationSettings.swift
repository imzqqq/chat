import SwiftUI

@available(iOS 14.0, *)
struct MentionsAndKeywordNotificationSettings: View {
    
    @ObservedObject var viewModel: NotificationSettingsViewModel
    
    var keywordSection: some View {
        SwiftUI.Section(
            header: FormSectionHeader(text: VectorL10n.settingsYourKeywords),
            footer: FormSectionFooter(text: VectorL10n.settingsMentionsAndKeywordsEncryptionNotice)
        ) {
            NotificationSettingsKeywords(viewModel: viewModel)
        }
    }
    var body: some View {
        NotificationSettings(
            viewModel: viewModel,
            bottomSection: keywordSection
        )
        .navigationTitle(VectorL10n.settingsMentionsAndKeywords)
    }
}

@available(iOS 14.0, *)
struct MentionsAndKeywords_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            MentionsAndKeywordNotificationSettings(
                viewModel: NotificationSettingsViewModel(
                    notificationSettingsService: MockNotificationSettingsService.example,
                    ruleIds: NotificationSettingsScreen.mentionsAndKeywords.pushRules
                )
            )
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}
