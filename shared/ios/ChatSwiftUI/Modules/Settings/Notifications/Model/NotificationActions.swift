import Foundation

/// The actions defined on a push rule, used in the static push rule definitions.
struct NotificationActions {
    let notify: Bool
    let highlight: Bool
    let sound: String?
    
    init(notify: Bool, highlight: Bool = false, sound: String? = nil) {
        self.notify = notify
        self.highlight = highlight
        self.sound = sound
    }
}
