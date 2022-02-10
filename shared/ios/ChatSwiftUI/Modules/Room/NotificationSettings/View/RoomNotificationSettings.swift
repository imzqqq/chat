import SwiftUI

@available(iOS 14.0.0, *)
struct RoomNotificationSettings: View {
    
    @ObservedObject var viewModel: RoomNotificationSettingsSwiftUIViewModel
    
    let presentedModally: Bool
    
    @ViewBuilder
    private var leftButton: some View {
        if presentedModally {
            Button(VectorL10n.cancel) {
                viewModel.process(viewAction: .cancel)
            }
        }
    }
    
    @ViewBuilder
    private var rightButton: some View {
        Button(VectorL10n.save) {
            viewModel.process(viewAction: .save)
        }
    }
    
    var body: some View {
        VectorForm {
            if  let avatarData = viewModel.viewState.avatarData as? AvatarInputProtocol {
                RoomNotificationSettingsHeader(
                    avatarData: avatarData,
                    displayName: viewModel.viewState.displayName
                )
            }
            SwiftUI.Section(
                header: FormSectionHeader(text: VectorL10n.roomNotifsSettingsNotifyMeFor),
                footer: FormSectionFooter(text: viewModel.viewState.roomEncryptedString)
            ) {
                ForEach(viewModel.viewState.notificationOptions) { option in
                    FormPickerItem(title: option.title, selected: viewModel.viewState.notificationState == option) {
                        viewModel.process(viewAction: .selectNotificationState(option))
                    }
                }
            }
        }
        .activityIndicator(show: viewModel.viewState.saving)
        .navigationBarTitle(VectorL10n.roomDetailsNotifs)
        .navigationBarItems(
            leading: leftButton,
            trailing: rightButton
        )
        .onAppear {
            viewModel.process(viewAction: .load)
        }
    }
}

@available(iOS 14.0, *)
struct RoomNotificationSettings_Previews: PreviewProvider {
    
    static let mockViewModel = RoomNotificationSettingsSwiftUIViewModel(
        roomNotificationService: MockRoomNotificationSettingsService.example,
        avatarData: MockAvatarInput.example,
        displayName: MockAvatarInput.example.displayName,
        roomEncrypted: true
    )
    
    static var previews: some View {
        Group {
            NavigationView {
                RoomNotificationSettings(viewModel: mockViewModel, presentedModally: true)
                    .navigationBarTitleDisplayMode(.inline)
                    .addDependency(MockAvatarService.example)
            }
            NavigationView {
                RoomNotificationSettings(viewModel: mockViewModel, presentedModally: true)
                    .navigationBarTitleDisplayMode(.inline)
                    .theme(ThemeIdentifier.dark)
                    .addDependency(MockAvatarService.example)
            }
        }
    }
}
