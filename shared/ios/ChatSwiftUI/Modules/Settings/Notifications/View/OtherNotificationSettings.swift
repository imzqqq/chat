import SwiftUI

@available(iOS 14.0, *)
struct OtherNotificationSettings: View {
    @ObservedObject var viewModel: NotificationSettingsViewModel
    
    var body: some View {
        NotificationSettings(viewModel: viewModel)
            .navigationTitle(VectorL10n.settingsOther)
    }
}

@available(iOS 14.0, *)
struct OtherNotifications_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            DefaultNotificationSettings(
                viewModel: NotificationSettingsViewModel(
                    notificationSettingsService: MockNotificationSettingsService.example,
                    ruleIds: NotificationSettingsScreen.other.pushRules
                )
            )
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}
