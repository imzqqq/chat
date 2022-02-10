import Foundation

/// `BubbleReactionsViewModelBuilder` enables to build a BubbleReactionsViewModel for a given `RoomBubbleCellData` and `MXKRoomBubbleComponent` index.
@objcMembers
final class BubbleReactionsViewModelBuilder: NSObject {
    
    func buildForFirstVisibleComponent(of roomBubbleCellData: RoomBubbleCellData) -> BubbleReactionsViewModel? {
        
        guard roomBubbleCellData.firstVisibleComponentIndex() != NSNotFound else {
            return nil
        }
        
        return self.build(from: roomBubbleCellData, componentIndex: roomBubbleCellData.firstVisibleComponentIndex())
    }
    
    func build(from roomBubbleCellData: RoomBubbleCellData, componentIndex: Int) -> BubbleReactionsViewModel? {
        
        let isCollapsableCellCollapsed = roomBubbleCellData.collapsable && roomBubbleCellData.collapsed
        
        guard isCollapsableCellCollapsed == false else {
            return nil
        }
        
        guard let bubbleComponents = roomBubbleCellData.bubbleComponents, componentIndex < roomBubbleCellData.bubbleComponents.count else {
            return nil
        }
        
        let bubbleComponent: MXKRoomBubbleComponent = bubbleComponents[componentIndex]
        
        guard let bubbleComponentEvent = bubbleComponent.event,
            bubbleComponentEvent.isRedactedEvent() == false,
            let componentEventId = bubbleComponentEvent.eventId,
            let cellDataReactions = roomBubbleCellData.reactions,
            let componentReactions = cellDataReactions[componentEventId] as? MXAggregatedReactions,
            let aggregatedReactions = componentReactions.withNonZeroCount() else {
                return nil
        }
        
        let showAllReactions = roomBubbleCellData.showAllReactions(forEvent: componentEventId)
        return BubbleReactionsViewModel(aggregatedReactions: aggregatedReactions,
                                        eventId: componentEventId,
                                        showAll: showAllReactions)
    }
}
