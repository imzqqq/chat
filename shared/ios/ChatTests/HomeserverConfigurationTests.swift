import XCTest

@testable import Chat

class HomeserverConfigurationTests: XCTestCase {

    override func setUpWithError() throws {
        // Put setup code here. This method is called before the invocation of each test method in the class.
    }

    override func tearDownWithError() throws {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
    }
    
    // MARK: - Tests
    
    func testHomeserverConfigurationBuilder() {
    
        let expectedJitsiServer = "your.jitsi.example.org"
        let expectedJitsiServerStringURL = "https://\(expectedJitsiServer)"
        let expectedDeprecatedJitsiServer = "your.deprecated.jitsi.example.org"
        let expectedE2EEEByDefaultEnabled = true
        let expectedDeprecatedE2EEEByDefaultEnabled = false
    
        let wellKnownDictionary: [String: Any] = [
            "m.homeserver": [
                 "base_url": "https://your.homeserver.org"
            ],
             "m.identity_server": [
                 "base_url": "https://your.identity-server.org"
            ],
            "im.vector.riot.e2ee" : [
                "default" : expectedDeprecatedE2EEEByDefaultEnabled
            ],
            "im.vector.riot.jitsi" : [
                "preferredDomain" : expectedDeprecatedJitsiServer
            ],
            "io.element.e2ee" : [
                "default" : expectedE2EEEByDefaultEnabled
            ],
            "io.element.jitsi" : [
                "preferredDomain" : expectedJitsiServer
            ]
        ]
        
        let wellKnown = MXWellKnown(fromJSON: wellKnownDictionary)
        
        let homeserverConfigurationBuilder = HomeserverConfigurationBuilder()
        let homeserverConfiguration = homeserverConfigurationBuilder.build(from: wellKnown)
    
        XCTAssertEqual(homeserverConfiguration.jitsi.serverDomain, expectedJitsiServer)
        XCTAssertEqual(homeserverConfiguration.jitsi.serverURL.absoluteString, expectedJitsiServerStringURL)
        XCTAssertEqual(homeserverConfiguration.isE2EEByDefaultEnabled, expectedE2EEEByDefaultEnabled)
    }
}
