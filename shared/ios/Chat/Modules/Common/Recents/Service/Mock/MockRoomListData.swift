import MatrixSDK

@objcMembers
public class MockRoomListData: MXRoomListData {
    
    public init(withRooms rooms: [MXRoomSummaryProtocol]) {
        super.init(rooms: rooms,
                   counts: MXStoreRoomListDataCounts(withRooms: rooms,
                                                     totalRoomsCount: rooms.count),
                   paginationOptions: .none)
    }
    
}
