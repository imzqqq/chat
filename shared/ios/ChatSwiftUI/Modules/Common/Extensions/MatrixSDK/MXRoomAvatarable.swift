import Foundation
extension MXRoom: Avatarable {
    var mxContentUri: String? {
        summary.avatar
    }
    
    var matrixItemId: String {
        roomId
    }
    
    var displayName: String? {
        summary.displayname
    }
    
}
