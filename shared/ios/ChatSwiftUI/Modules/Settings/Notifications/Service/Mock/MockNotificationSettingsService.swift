import Foundation
import Combine

@available(iOS 14.0, *)
class MockNotificationSettingsService: NotificationSettingsServiceType, ObservableObject {
    static let example = MockNotificationSettingsService()
    
    @Published var keywords = Set<String>()
    @Published var rules = [NotificationPushRuleType]()
    @Published var contentRules = [NotificationPushRuleType]()
    
    var contentRulesPublisher: AnyPublisher<[NotificationPushRuleType], Never> {
        $contentRules.eraseToAnyPublisher()
    }
    
    var keywordsPublisher: AnyPublisher<Set<String>, Never> {
        $keywords.eraseToAnyPublisher()
    }
    
    var rulesPublisher: AnyPublisher<[NotificationPushRuleType], Never> {
        $rules.eraseToAnyPublisher()
    }
    
    func add(keyword: String, enabled: Bool) {
        keywords.insert(keyword)
    }
    
    func remove(keyword: String) {
        keywords.remove(keyword)
    }
    
    func updatePushRuleActions(for ruleId: String, enabled: Bool, actions: NotificationActions?) {
        
    }
}
