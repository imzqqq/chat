import UIKit

@objcMembers
class RoomCreationIntroCell: MXKRoomBubbleTableViewCell {
    
    // MARK: - Constants
    
    private enum Sizing {
        static var sizes = Set<SizingViewHeight>()
        static let view: RoomCreationIntroCell = RoomCreationIntroCell(style: .default, reuseIdentifier: nil)
    }
        
    static let tapOnAvatarView = "RoomCreationIntroCellTapOnAvatarView"
    static let tapOnAddTopic = "RoomCreationIntroCellTapOnAddTopic"
    static let tapOnAddParticipants = "RoomCreationIntroCellTapOnAddParticipants"
    
    // MARK: - Properties
        
    private weak var bubbleCellContentView: RoomCreationIntroCellContentView?
    
    // MARK: - Setup
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        self.commonInit()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func commonInit() {
        
        self.selectionStyle = .none
        self.setupContentView()
        self.update(theme: ThemeService.shared().theme)
        
        super.setupViews()
    }
    
    // MARK: - Public
    
    func update(theme: Theme) {
        self.bubbleCellContentView?.update(theme: theme)
    }
    
    // MARK: - Overrides
    
    override class func defaultReuseIdentifier() -> String! {
        return String(describing: self)
    }
    
    override class func height(for cellData: MXKCellData!, withMaximumWidth maxWidth: CGFloat) -> CGFloat {
        guard let cellData = cellData else {
            return 0
        }
        
        guard let roomBubbleCellData = cellData as? MXKRoomBubbleCellData else {
            return 0
        }
        
        let height: CGFloat
        
        let sizingViewHeight = self.findOrCreateSizingViewHeight(from: roomBubbleCellData)
        
        if let cachedHeight = sizingViewHeight.heights[maxWidth] {
            height = cachedHeight
        } else {
            height = self.contentViewHeight(for: roomBubbleCellData, fitting: maxWidth)
            sizingViewHeight.heights[maxWidth] = height
        }
        
        return height
    }
    
    override func render(_ cellData: MXKCellData!) {
        super.render(cellData)
        
        guard let bubbleCellContentView = self.bubbleCellContentView else {
            MXLog.debug("[RoomCreationIntroCell] Fail to load content view")
            return
        }
        
        guard let bubbleData = self.bubbleData,
              let viewData = self.viewData(from: bubbleData) else {
            MXLog.debug("[RoomCreationIntroCell] Fail to render \(String(describing: cellData))")
            return
        }
        
        bubbleCellContentView.fill(with: viewData)
    }
    
    // MARK: - Private
    
    private static func findOrCreateSizingViewHeight(from bubbleData: MXKRoomBubbleCellData) -> SizingViewHeight {
        
        let sizingViewHeight: SizingViewHeight
        let bubbleDataHashValue = bubbleData.hashValue
        
        if let foundSizingViewHeight = self.Sizing.sizes.first(where: { (sizingViewHeight) -> Bool in
            return sizingViewHeight.uniqueIdentifier == bubbleDataHashValue
        }) {
            sizingViewHeight = foundSizingViewHeight
        } else {
            sizingViewHeight = SizingViewHeight(uniqueIdentifier: bubbleDataHashValue)
        }
        
        return sizingViewHeight
    }
    
    private class func sizingView() -> RoomCreationIntroCell {
        return self.Sizing.view
    }
    
    private static func contentViewHeight(for cellData: MXKCellData, fitting width: CGFloat) -> CGFloat {
        let sizingView = self.sizingView()
        
        sizingView.render(cellData)
        sizingView.layoutIfNeeded()
        
        let fittingSize = CGSize(width: width, height: UIView.layoutFittingCompressedSize.height)
        
        return sizingView.systemLayoutSizeFitting(fittingSize).height
    }
    
    private func setupContentView() {
        guard self.bubbleCellContentView == nil else {
            return
        }
            
        let bubbleCellContentView = RoomCreationIntroCellContentView.instantiate()
        self.contentView.vc_addSubViewMatchingParent(bubbleCellContentView)
        self.bubbleCellContentView = bubbleCellContentView
        
        bubbleCellContentView.roomAvatarView?.action = { [weak self] in
            self?.notifyDelegate(with: RoomCreationIntroCell.tapOnAvatarView)
        }
        
        bubbleCellContentView.didTapTopic = { [weak self] in
            self?.notifyDelegate(with: RoomCreationIntroCell.tapOnAddTopic)
        }
        
        bubbleCellContentView.didTapAddParticipants = { [weak self] in
            self?.notifyDelegate(with: RoomCreationIntroCell.tapOnAddParticipants)
        }
    }
    
    
    private func viewData(from bubbleData: MXKRoomBubbleCellData) -> RoomCreationIntroViewData? {
        
        guard let session = bubbleData.mxSession, let roomId = bubbleData.roomId, let room = session.room(withRoomId: roomId) else {
            return nil
        }
        
        guard let roomSummary = room.summary else {
            return nil
        }
                        
        let discussionType: DiscussionType
        
        if roomSummary.isDirect {
            if roomSummary.membersCount.members > 2 {
                discussionType = .directMessage
            } else {
                discussionType = .multipleDirectMessage
            }
        } else {
            discussionType = .room(topic: roomSummary.topic)
        }
        
        let displayName = roomSummary.displayname ?? ""
        
        let roomAvatarViewData = RoomAvatarViewData(roomId: roomId,
                                                    displayName: displayName,
                                                    avatarUrl: room.summary.avatar, mediaManager: session.mediaManager)
        
        return RoomCreationIntroViewData(dicussionType: discussionType, roomDisplayName: displayName, avatarViewData: roomAvatarViewData)
    }
    
    private func notifyDelegate(with actionIdentifier: String) {
        self.delegate.cell(self, didRecognizeAction: actionIdentifier, userInfo: nil)
    }
}
