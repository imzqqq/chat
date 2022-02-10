import UIKit

class SeparatorConfiguredCollectionViewListCell: UICollectionViewListCell {
    override func updateConstraints() {
        super.updateConstraints()

        NSLayoutConstraint.activate([
            separatorLayoutGuide.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor),
            separatorLayoutGuide.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor)
        ])
    }
}
