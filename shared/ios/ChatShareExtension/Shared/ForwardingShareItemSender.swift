import Foundation

@objcMembers
class ForwardingShareItemSender: NSObject, ShareItemSenderProtocol {
    
    static let errorDomain = "ForwardingShareItemSenderErrorDomain"

    enum ErrorCode: Int {
        case eventNotSentYet
    }
    
    private let event: MXEvent
    
    weak var delegate: ShareItemSenderDelegate?
    
    @objc public init(withEvent event: MXEvent) {
        self.event = event
    }
    
    func sendItems(to rooms: [MXRoom], success: @escaping () -> Void, failure: @escaping ([Error]) -> Void) {
        guard event.sentState == MXEventSentStateSent else {
            MXLog.error("[ForwardingShareItemSender] Cannot forward unsent event")
            failure([NSError(domain: Self.errorDomain,
                            code: ErrorCode.eventNotSentYet.rawValue,
                            userInfo: nil)])
            return
        }
        
        self.delegate?.shareItemSenderDidStartSending(self)
        
        var errors = [Error]()
        
        let dispatchGroup = DispatchGroup()
        for room in rooms {
            dispatchGroup.enter()
            
            var localEcho: MXEvent?
            room.sendMessage(withContent: event.content, localEcho: &localEcho) { result in
                switch result {
                case .failure(let innerError):
                    errors.append(innerError)
                default:
                    break
                }
                
                dispatchGroup.leave()
            }
        }
        
        dispatchGroup.notify(queue: DispatchQueue.main) {
            guard errors.count == 0 else {
                failure(errors)
                return
            }
            
            success()
        }
    }
}
