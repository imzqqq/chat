import Foundation

extension OperationQueue {
    
    class func vc_createSerialOperationQueue(name: String? = nil) -> OperationQueue {
        let coordinatorDelegateQueue = OperationQueue()
        coordinatorDelegateQueue.name = name
        coordinatorDelegateQueue.maxConcurrentOperationCount = 1
        return coordinatorDelegateQueue
    }
    
    func vc_pause() {
        self.isSuspended = true
    }
    
    func vc_resume() {
        self.isSuspended = false
    }
}
