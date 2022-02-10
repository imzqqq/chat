import SwiftUI

@available(iOS 14.0, *)
struct TemplateRoomChatBubbleImage: View {

    // MARK: - Properties
    
    // MARK: Private
    
    @Environment(\.theme) private var theme: ThemeSwiftUI
    
    // MARK: Public
    
    let imageContent: TemplateRoomChatMessageImageContent
    
    var body: some View {
        EmptyView()
    }
}

// MARK: - Previews

@available(iOS 14.0, *)
struct TemplateRoomChatBubbleImage_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
        // TODO: New to our SwiftUI Template? Why not implement the image item in the bubble here?
    }
}
