import Foundation

protocol NotificationPushRuleType {
    var ruleId: String! { get }
    var enabled: Bool { get }
    func matches(standardActions: NotificationStandardActions?) -> Bool
}
