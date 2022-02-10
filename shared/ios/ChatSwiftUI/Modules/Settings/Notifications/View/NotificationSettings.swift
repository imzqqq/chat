import SwiftUI

/// Renders the push rule settings that can be enabled/disable.
///
/// Also renders an optional bottom section.
/// Used in the case of keywords, for the keyword chips and input.
@available(iOS 14.0, *)
struct NotificationSettings<BottomSection: View>: View {
    
    @ObservedObject var viewModel: NotificationSettingsViewModel
    
    var bottomSection: BottomSection?
    
    var body: some View {
        VectorForm {
            SwiftUI.Section(
                header: FormSectionHeader(text: VectorL10n.settingsNotifyMeFor)
            ) {
                ForEach(viewModel.viewState.ruleIds) { ruleId in
                    let checked = viewModel.viewState.selectionState[ruleId] ?? false
                    FormPickerItem(title: ruleId.title, selected: checked) {
                        viewModel.update(ruleID: ruleId, isChecked: !checked)
                    }
                }
            }
            bottomSection
        }
        .activityIndicator(show: viewModel.viewState.saving)
    }
}

@available(iOS 14.0, *)
extension NotificationSettings where BottomSection == EmptyView {
    init(viewModel: NotificationSettingsViewModel) {
        self.init(viewModel: viewModel, bottomSection: nil)
    }
}

@available(iOS 14.0, *)
struct NotificationSettings_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ForEach(NotificationSettingsScreen.allCases) { screen in
                NavigationView {
                    NotificationSettings(
                        viewModel: NotificationSettingsViewModel(
                            notificationSettingsService: MockNotificationSettingsService.example,
                            ruleIds: screen.pushRules
                        )
                    )
                    .navigationBarTitleDisplayMode(.inline)
                }
            }
        }
    }
}
