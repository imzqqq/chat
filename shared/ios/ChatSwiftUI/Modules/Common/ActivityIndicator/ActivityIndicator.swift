import SwiftUI

@available(iOS 14.0, *)
/// A visual cue to user that something is in progress.
struct ActivityIndicator: View {
    
    private enum Constants {
        static let backgroundColor = Color(UIColor(white: 0.8, alpha: 0.9))
    }
    
    var body: some View {
        ProgressView()
            .progressViewStyle(CircularProgressViewStyle(tint: Color.white))
            .padding()
            .background(Constants.backgroundColor)
            .cornerRadius(5)
            
    }
}

@available(iOS 14.0, *)
struct ActivityIndicator_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            Text("Hello World!")
                .activityIndicator(show: true)
            Text("Hello World!")
                .activityIndicator(show: false)
        }
    }
}
