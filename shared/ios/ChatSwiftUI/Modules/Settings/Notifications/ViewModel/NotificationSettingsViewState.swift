import Foundation

struct NotificationSettingsViewState {
    var saving: Bool
    var ruleIds: [NotificationPushRuleId]
    var selectionState: [NotificationPushRuleId: Bool]
    var keywords = [String]()
}
