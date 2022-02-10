export enum NotificationColor {
    // Inverted (None -> Red) because we do integer comparisons on this
    None, // nothing special
    // TODO: Remove bold with notifications: https://github.com/vector-im/element-web/issues/14227
    Bold, // no badge, show as unread
    Grey, // unread notified messages
    Red,  // unread pings
    Unsent, // some messages failed to send
}
