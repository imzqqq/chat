import Foundation

@objcMembers
final class Row: NSObject {
    
    let tag: Int
    
    init(withTag tag: Int) {
        self.tag = tag
        super.init()
    }
    
    static func row(withTag tag: Int) -> Row {
        return Row(withTag: tag)
    }
    
}
