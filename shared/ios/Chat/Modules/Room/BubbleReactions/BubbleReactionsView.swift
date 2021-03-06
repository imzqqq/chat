import Foundation
import MatrixSDK
import Reusable
import DGCollectionViewLeftAlignFlowLayout

@objcMembers
final class BubbleReactionsView: UIView, NibOwnerLoadable {
    
    // MARK: - Constants
    
    private enum Constants {
        static let minimumInteritemSpacing: CGFloat = 6.0
        static let minimumLineSpacing: CGFloat = 2.0
    }
    
    // MARK: - Properties
    
    // MARK: Outlets

    @IBOutlet private weak var collectionView: UICollectionView!
    
    // MARK: Private
    
    private var reactionsViewData: [BubbleReactionViewData] = []
    private var showAllButtonState: BubbleReactionsViewState.ShowAllButtonState = .none
    private var theme: Theme?
    
    // MARK: Public
    
    // Do not use `BubbleReactionsViewModelType` here due to Objective-C incompatibily
    var viewModel: BubbleReactionsViewModel? {
        didSet {
            self.viewModel?.viewDelegate = self
            self.viewModel?.process(viewAction: .loadData)
        }
    }
    
    // MARK: - Setup
    
    private func commonInit() {
        self.setupCollectionView()
        self.setupLongPressGestureRecognizer()
    }
    
    convenience init() {
        self.init(frame: CGRect.zero)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.loadNibContent()
        self.commonInit()
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
        self.loadNibContent()
        self.commonInit()
    }    
    
    // MARK: - Public
    
    func update(theme: Theme) {
        self.theme = theme
        self.collectionView.reloadData()
    }

    // MARK: - Private
    
    private func setupCollectionView() {
        self.collectionView.isScrollEnabled = false
        self.collectionView.delegate = self
        self.collectionView.dataSource = self
        self.collectionView.collectionViewLayout = DGCollectionViewLeftAlignFlowLayout()
        
        if let collectionViewFlowLayout = self.collectionView.collectionViewLayout as? UICollectionViewFlowLayout {
            collectionViewFlowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
            collectionViewFlowLayout.minimumInteritemSpacing = Constants.minimumInteritemSpacing
            collectionViewFlowLayout.minimumLineSpacing = Constants.minimumLineSpacing
        }
        
        self.collectionView.register(cellType: BubbleReactionViewCell.self)
        self.collectionView.register(cellType: BubbleReactionActionViewCell.self)
        self.collectionView.reloadData()
    }
    
    private func setupLongPressGestureRecognizer() {
        let gestureRecognizer = UILongPressGestureRecognizer(target: self, action: #selector(handleLongPress(_:)))
        gestureRecognizer.delaysTouchesBegan = true
        self.collectionView.addGestureRecognizer(gestureRecognizer)
    }
    
    @objc private func handleLongPress(_ gestureRecognizer: UILongPressGestureRecognizer) {
        guard gestureRecognizer.state == .began else {
            return
        }
        self.viewModel?.process(viewAction: .longPress)
    }
    
    private func fill(reactionsViewData: [BubbleReactionViewData], showAllButtonState: BubbleReactionsViewState.ShowAllButtonState) {
        self.reactionsViewData = reactionsViewData
        self.showAllButtonState = showAllButtonState
        self.collectionView.reloadData()
        self.collectionView.collectionViewLayout.invalidateLayout()
    }

    private func actionButtonString() -> String {
        let actionString: String
        switch self.showAllButtonState {
        case .showAll:
            actionString = VectorL10n.roomEventActionReactionShowAll
        case .showLess:
            actionString = VectorL10n.roomEventActionReactionShowLess
        case .none:
            actionString = ""
        }

        return actionString
    }
}

// MARK: - UICollectionViewDataSource
extension BubbleReactionsView: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        // "Show all" or "Show less" is a cell in the same section as reactions cells
        let additionalItems = self.showAllButtonState == .none ? 0 : 1

        return self.reactionsViewData.count + additionalItems
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if indexPath.row < self.reactionsViewData.count {
            let cell: BubbleReactionViewCell = collectionView.dequeueReusableCell(for: indexPath)

            if let theme = self.theme {
                cell.update(theme: theme)
            }

            let viewData = self.reactionsViewData[indexPath.row]
            cell.fill(viewData: viewData)

            return cell
        } else {
            let cell: BubbleReactionActionViewCell = collectionView.dequeueReusableCell(for: indexPath)

            if let theme = self.theme {
                cell.update(theme: theme)
            }

            let actionString = self.actionButtonString()
            cell.fill(actionString: actionString)

            return cell
        }
    }
}

// MARK: - UICollectionViewDelegate
extension BubbleReactionsView: UICollectionViewDelegate {
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if indexPath.row < self.reactionsViewData.count {
            self.viewModel?.process(viewAction: .tapReaction(index: indexPath.row))
        } else {
            switch self.showAllButtonState {
            case .showAll:
                self.viewModel?.process(viewAction: .tapShowAction(action: .showAll))
            case .showLess:
                self.viewModel?.process(viewAction: .tapShowAction(action: .showLess))
            case .none:
                break
            }
        }
    }
}

// MARK: - BubbleReactionsViewModelViewDelegate
extension BubbleReactionsView: BubbleReactionsViewModelViewDelegate {
    
    func bubbleReactionsViewModel(_ viewModel: BubbleReactionsViewModel, didUpdateViewState viewState: BubbleReactionsViewState) {
        switch viewState {
        case .loaded(reactionsViewData: let reactionsViewData, showAllButtonState: let showAllButtonState):
            self.fill(reactionsViewData: reactionsViewData, showAllButtonState: showAllButtonState)
        }
    }
}
