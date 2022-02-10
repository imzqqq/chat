import Foundation

/// `AppCoordinatorType` is a protocol describing a Coordinator that handles application navigation flow. 
protocol AppCoordinatorType: Coordinator {
    
    func open(url: URL, options: [UIApplication.OpenURLOptionsKey: Any]) -> Bool
}
