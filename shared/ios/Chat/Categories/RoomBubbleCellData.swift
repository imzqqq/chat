import Foundation

@objc extension RoomBubbleCellData {
    
    /// Gathers all collapsable events in both directions (previous and next)
    /// - Returns: Array of events containing collapsable events in both sides.
    func allLinkedEvents() -> [MXEvent] {
        var result: [MXEvent] = []
        
        //  add prev linked events
        var prevBubbleData = prevCollapsableCellData
        while prevBubbleData != nil {
            if let events = prevBubbleData?.events {
                result.append(contentsOf: events)
            }
            prevBubbleData = prevBubbleData?.prevCollapsableCellData
        }
        
        //  add self events
        result.append(contentsOf: events)
        
        //  add next linked events
        var nextBubbleData = nextCollapsableCellData
        while nextBubbleData != nil {
            if let events = nextBubbleData?.events {
                result.append(contentsOf: events)
            }
            nextBubbleData = nextBubbleData?.nextCollapsableCellData
        }
        
        return result
    }
    
}
