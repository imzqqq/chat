import Foundation

struct TemplateUserProfileViewState: BindableState {
    let avatar: AvatarInputProtocol?
    let displayName: String?
    var presence: TemplateUserProfilePresence
    var count: Int
}
