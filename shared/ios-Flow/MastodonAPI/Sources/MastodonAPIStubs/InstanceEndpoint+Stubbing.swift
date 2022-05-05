// Copyright © 2020 Metabolist. All rights reserved.

import Foundation
import MastodonAPI
import Stubbing

extension InstanceEndpoint: Stubbing {
    public func data(url: URL) -> Data? {
        StubData.instance
    }
}
