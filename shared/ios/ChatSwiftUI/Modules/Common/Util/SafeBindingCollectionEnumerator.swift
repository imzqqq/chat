import SwiftUI

/**
 Used to avoid crashes when enumerating through bindings in the AnswerOptions ForEach
 https://stackoverflow.com/q/65375372
 Replace with Swift 5.5 bindings enumerator later.
 */
@available(iOS 14.0, *)
struct SafeBindingCollectionEnumerator<T: RandomAccessCollection & MutableCollection, C: View>: View {
    
    typealias BoundElement = Binding<T.Element>
    private let binding: BoundElement
    private let content: (BoundElement) -> C
    
    init(_ binding: Binding<T>, index: T.Index, @ViewBuilder content: @escaping (BoundElement) -> C) {
        self.content = content
        self.binding = .init(get: { binding.wrappedValue[index] },
                             set: { binding.wrappedValue[index] = $0 })
    }
    
    var body: some View {
        content(binding)
    }
}
