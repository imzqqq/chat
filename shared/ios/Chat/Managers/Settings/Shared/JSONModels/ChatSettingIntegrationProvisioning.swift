import Foundation

/// Model for "im.vector.setting.integration_provisioning"
/// https://github.com/vector-im/riot-meta/blob/master/spec/settings.md#selecting-no-provisioning-for-integration-managers
struct ChatSettingIntegrationProvisioning {
    let enabled: Bool
}

extension ChatSettingIntegrationProvisioning: Decodable {
    enum CodingKeys: String, CodingKey {
        case enabled
    }
}
