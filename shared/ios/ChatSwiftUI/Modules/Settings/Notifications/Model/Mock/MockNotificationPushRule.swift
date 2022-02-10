import Foundation

struct MockNotificationPushRule: NotificationPushRuleType {
    var ruleId: String!
    var enabled: Bool
    func matches(standardActions: NotificationStandardActions?) -> Bool {
        return false
    }
}
