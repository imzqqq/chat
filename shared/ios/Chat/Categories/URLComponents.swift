import Foundation

extension URLComponents {
    
    func vc_getQueryItem(with name: String) -> URLQueryItem? {
        return self.queryItems?.first(where: { $0.name == name })
    }
    
    func vc_getQueryItemValue(for name: String) -> String? {
        return self.vc_getQueryItem(with: name)?.value
    }
}
