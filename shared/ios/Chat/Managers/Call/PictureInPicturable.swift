import Foundation

@objc protocol PictureInPicturable {
    
    @objc optional func willEnterPiP()
    @objc optional func didEnterPiP()
    
    @objc optional func willExitPiP()
    @objc optional func didExitPiP()
    
}
