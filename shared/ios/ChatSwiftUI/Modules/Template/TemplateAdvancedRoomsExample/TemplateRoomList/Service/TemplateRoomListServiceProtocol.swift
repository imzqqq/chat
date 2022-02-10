import Foundation
import Combine

@available(iOS 14.0, *)
protocol TemplateRoomListServiceProtocol {
    var roomsSubject: CurrentValueSubject<[TemplateRoomListRoom], Never> { get }
}
