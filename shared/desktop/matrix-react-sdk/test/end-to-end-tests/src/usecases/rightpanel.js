module.exports.openRoomRightPanel = async function(session) {
    try {
        await session.query('.mx_RoomHeader .mx_RightPanel_headerButton_highlight[aria-label="Room Info"]');
    } catch (e) {
        // If the room summary is not yet open, open it
        const roomSummaryButton = await session.query('.mx_RoomHeader .mx_AccessibleButton[aria-label="Room Info"]');
        await roomSummaryButton.click();
    }
};

module.exports.goBackToRoomSummaryCard = async function(session) {
    for (let i = 0; i < 5; i++) {
        try {
            const backButton = await session.query(".mx_BaseCard_back", 500);
            // Right panel is open to the wrong thing - go back up to the Room Summary Card
            // Sometimes our tests have this opened to MemberInfo
            await backButton.click();
        } catch (e) {
            // explicitly check for TimeoutError as this sometimes returned
            // `Error: Node is detached from document` due to a re-render race or similar
            if (e.name === "TimeoutError") {
                break; // stop trying to go further back
            }
        }
    }
};

module.exports.openRoomSummaryCard = async function(session) {
    await module.exports.openRoomRightPanel(session);
    await module.exports.goBackToRoomSummaryCard(session);
};
