import SwiftUI

@available(iOS 14.0, *)
struct DefaultNotificationSettings: View {
    
    @ObservedObject var viewModel: NotificationSettingsViewModel
    
    var body: some View {
        NotificationSettings(viewModel: viewModel)
            .navigationBarTitle(VectorL10n.settingsDefault)
    }
}

@available(iOS 14.0, *)
struct DefaultNotifications_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            DefaultNotificationSettings(
                viewModel: NotificationSettingsViewModel(
                    notificationSettingsService: MockNotificationSettingsService.example,
                    ruleIds: NotificationSettingsScreen.defaultNotifications.pushRules
                )
            )
            .navigationBarTitleDisplayMode(.inline)
        }

    }
}
