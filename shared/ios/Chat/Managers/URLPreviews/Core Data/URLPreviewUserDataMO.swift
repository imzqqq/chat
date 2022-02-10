import CoreData

extension URLPreviewUserDataMO {
    convenience init(context: NSManagedObjectContext, eventID: String, roomID: String, dismissed: Bool) {
        self.init(context: context)
        self.eventID = eventID
        self.roomID = roomID
        self.dismissed = dismissed
    }
}
