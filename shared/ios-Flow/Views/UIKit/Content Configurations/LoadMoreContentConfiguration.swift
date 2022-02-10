import UIKit
import ViewModels

struct LoadMoreContentConfiguration {
    let viewModel: LoadMoreViewModel
}

extension LoadMoreContentConfiguration: UIContentConfiguration {
    func makeContentView() -> UIView & UIContentView {
        LoadMoreView(configuration: self)
    }

    func updated(for state: UIConfigurationState) -> LoadMoreContentConfiguration {
        self
    }
}
