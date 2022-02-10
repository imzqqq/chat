import UIKit
import Reusable

@objcMembers
final class LaunchLoadingView: UIView, NibLoadable, Themable {
    
    // MARK: - Constants
    
    private enum LaunchAnimation {
        static let duration: TimeInterval = 3.0
        static let repeatCount = Float.greatestFiniteMagnitude
    }
    
    // MARK: - Properties
    
    @IBOutlet private weak var animationView: ElementView!
    private var animationTimeline: Timeline_1!
    
    // MARK: - Setup
    
    static func instantiate() -> LaunchLoadingView {
        let view = LaunchLoadingView.loadFromNib()
        return view
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let animationTimeline = Timeline_1(view: self.animationView, duration: LaunchAnimation.duration, repeatCount: LaunchAnimation.repeatCount)
        animationTimeline.play()
        self.animationTimeline = animationTimeline
    }
    
    // MARK: - Public
    
    func update(theme: Theme) {
        self.backgroundColor = theme.backgroundColor
        self.animationView.backgroundColor = theme.backgroundColor
    }
}
