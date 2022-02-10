import Foundation
import DesignKit

// Conformance of MXPushRule to the abstraction `NotificationPushRule` for use in `NotificationSettingsViewModel`.
extension MXPushRule: NotificationPushRuleType {
    
    /// Given a rule, check it match the actions in the static definition.
    /// - Parameter standardActions: The standard actions to match against.
    /// - Returns: Wether `this` rule matches the standard actions.
    func matches(standardActions: NotificationStandardActions?) -> Bool {
        guard let standardActions = standardActions else {
            return false
        }
        if !enabled && standardActions == .disabled {
            return true
        }
        
        if enabled,
           let actions = standardActions.actions,
           highlight == actions.highlight,
           sound == actions.sound,
           notify == actions.notify,
           dontNotify == !actions.notify {
            return true
        }
        return false
    }
    
    private func getAction(actionType: MXPushRuleActionType, tweakType: String? = nil) -> MXPushRuleAction? {
        guard let actions = actions as? [MXPushRuleAction] else {
            return nil
        }
        
        return actions.first { action in
            var match = action.actionType == actionType
            if let tweakType = tweakType,
               let actionTweak = action.parameters?["set_tweak"] as? String {
                match = match && (tweakType == actionTweak)
            }
            return match
        }
    }
    
    var highlight: Bool {
        guard let action = getAction(actionType: MXPushRuleActionTypeSetTweak, tweakType: "highlight") else {
            return false
        }
        if let highlight = action.parameters["value"] as? Bool {
            return highlight
        }
        return true
    }
    
    var sound: String? {
        guard let action = getAction(actionType: MXPushRuleActionTypeSetTweak, tweakType: "sound") else {
            return nil
        }
        return action.parameters["value"] as? String
    }
    
    var notify: Bool {
        return getAction(actionType: MXPushRuleActionTypeNotify) != nil
    }
    
    var dontNotify: Bool {
        return getAction(actionType: MXPushRuleActionTypeDontNotify) != nil
    }
}
