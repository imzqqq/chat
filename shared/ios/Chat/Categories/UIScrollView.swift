import Foundation

extension UIScrollView {
    
    /// Scroll to the given view, which must be a view in the scrollView.
    /// - Parameters:
    ///   - view: The view to scroll
    ///   - insets: Insets for the scroll area. Provide negative values for more visible area than the view's frame
    ///   - animated: animate the scroll
    @objc func vc_scroll(to view: UIView, with insets: UIEdgeInsets = .zero, animated: Bool = true) {
        //  find the view's frame in the scrollView with given insets
        let rect = view.convert(view.frame, to: self).inset(by: insets)
        DispatchQueue.main.async {
            self.scrollRectToVisible(rect, animated: animated)
        }
    }
    
}
