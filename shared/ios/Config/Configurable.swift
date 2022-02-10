import Foundation

import Foundation
import MatrixKit

/// Configurable expose settings app and its entensions must use.
@objc protocol Configurable {
    // MARK: - Global settings
    func setupSettings()
    
    // MARK: - Per matrix session settings
    func setupSettings(for matrixSession: MXSession)
    
    // MARK: - Per loaded matrix session settings
    func setupSettingsWhenLoaded(for matrixSession: MXSession)
}
