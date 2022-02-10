import Foundation

/// A protocol that any class or struct can conform to
/// so that it can easily produce avatar data.
/// 
/// E.g. MXRoom, MxUser can conform to this making it
/// easy to grab the avatar data for display.
protocol Avatarable: AvatarInputProtocol { }
extension Avatarable {
    var avatarData: AvatarInput {
        AvatarInput(
            mxContentUri: mxContentUri,
            matrixItemId: matrixItemId,
            displayName: displayName
        )
    }
}
