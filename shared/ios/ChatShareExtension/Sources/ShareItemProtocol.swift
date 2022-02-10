import Foundation

@objc public enum ShareItemType: UInt {
    case fileURL, text, URL, image, video, movie, unknown
}

@objc public protocol ShareItemProtocol {
    var type: ShareItemType { get }
}
