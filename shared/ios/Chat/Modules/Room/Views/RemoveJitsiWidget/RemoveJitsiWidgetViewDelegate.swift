import Foundation

@objc
protocol RemoveJitsiWidgetViewDelegate: AnyObject {
    
    /// Tells the delegate that the user complete sliding on the view
    /// - Parameter view: The view instance
    func removeJitsiWidgetViewDidCompleteSliding(_ view: RemoveJitsiWidgetView)
}
