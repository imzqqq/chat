import Foundation

extension UILabel {
    
    @objc func vc_setText(_ text: String, withLineSpacing lineSpacing: CGFloat, alignement: NSTextAlignment) {
        
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.lineSpacing = lineSpacing
        paragraphStyle.alignment = alignement
        
        let attributeString = NSAttributedString(string: text, attributes: [NSAttributedString.Key.paragraphStyle: paragraphStyle])
        
        self.attributedText = attributeString
    }
    
    // Fix multiline label height with auto layout. After performing orientation multiline label text appears on one line.
    // For more information see https://www.objc.io/issues/3-views/advanced-auto-layout-toolbox/#intrinsic-content-size-of-multi-line-text
    @objc func vc_fixMultilineHeight() {
        let width = self.frame.size.width
        
        if self.preferredMaxLayoutWidth != width {
           self.preferredMaxLayoutWidth = width
        }
    }
}
