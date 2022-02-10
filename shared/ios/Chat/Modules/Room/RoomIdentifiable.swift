import Foundation

/// `RoomIdentifiable` describes an object tied to a specific room id.
/// Useful to identify existing objects that should be removed when the user leaves a room for example.
protocol RoomIdentifiable {
    var roomId: String? { get }
    var mxSession: MXSession? { get }
}
