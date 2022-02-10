import Foundation

/**
 Object container storing references weakly. Ideal for implementing simple multiple delegation.
 */
struct DelegateContainer {
    
    private let hashTable: NSHashTable<AnyObject>
    
    var delegates: [AnyObject] {
        return hashTable.allObjects
    }
    
    init() {
        hashTable = NSHashTable(options: .weakMemory)
    }
    
    func registerDelegate(_ delegate: AnyObject) {
        hashTable.add(delegate)
    }
    
    func deregisterDelegate(_ delegate: AnyObject) {
        hashTable.remove(delegate)
    }
    
    func notifyDelegatesWithBlock(_ block: (AnyObject) -> Void) {
        for delegate in hashTable.allObjects {
            block(delegate)
        }
    }
}
