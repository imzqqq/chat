import XCTest

@testable import Chat

class EmojiStoreTests: XCTestCase {

    private lazy var store = loadStore()

    // MARK: - Tests

    func testFinds💯WhenSearchingForHundred() {
        find("hundred", expect: "💯")
    }

    func testFinds💯WhenSearchingFor100() {
        find("100", expect: "💯")
    }

    func testFinds2️⃣WhenSearchingForTwo() {
        find("two", expect: "2️⃣")
    }

    func testFinds2️⃣WhenSearchingFor2() {
        find("2", expect: "2️⃣")
    }

    // MARK: - Helpers

    private func loadStore() -> EmojiStore {
        let store = EmojiStore()
        let emojiService = EmojiMartService()
        let expectation = self.expectation(description: "The wai-ai-ting is the hardest part")

        emojiService.getEmojiCategories { response in
            switch response {
            case .success(let categories):
                store.set(categories)
                expectation.fulfill()
            case .failure(let error):
                XCTFail("Failed to load emojis: \(error)")
            }
        }

        waitForExpectations(timeout: 2) { error in
            XCTAssertNil(error)
        }

        return store
    }

    private func find(_ searchText: String, expect emoji: String) {
        let emojis = store.findEmojiItemsSortedByCategory(with: searchText).flatMap { $0.emojis.map { $0.value } }
        XCTAssert(emojis.contains(emoji), "Search text \"\(searchText)\" should find \"\(emoji)\" but only found \(emojis)")
    }

}
