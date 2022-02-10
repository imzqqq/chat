import CoreData

extension URLPreviewDataMO {
    convenience init(context: NSManagedObjectContext, preview: URLPreviewData, creationDate: Date) {
        self.init(context: context)
        update(from: preview, on: creationDate)
    }
    
    func update(from preview: URLPreviewData, on date: Date) {
        url = preview.url
        siteName = preview.siteName
        title = preview.title
        text = preview.text
        image = preview.image
        
        creationDate = date
    }
    
    func preview(for event: MXEvent) -> URLPreviewData? {
        guard let url = url else { return nil }
        
        let viewData = URLPreviewData(url: url,
                                      eventID: event.eventId,
                                      roomID: event.roomId,
                                      siteName: siteName,
                                      title: title,
                                      text: text)
        viewData.image = image as? UIImage
        
        return viewData
    }
}
