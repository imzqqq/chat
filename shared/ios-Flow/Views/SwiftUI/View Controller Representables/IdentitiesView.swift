import SwiftUI
import ViewModels

struct IdentitiesView: UIViewControllerRepresentable {
    let viewModelClosure: () -> IdentitiesViewModel
    @EnvironmentObject var rootViewModel: RootViewModel

    func makeUIViewController(context: Context) -> IdentitiesViewController {
        IdentitiesViewController(viewModel: viewModelClosure(), rootViewModel: rootViewModel)
    }

    func updateUIViewController(_ uiViewController: IdentitiesViewController, context: Context) {

    }
}
