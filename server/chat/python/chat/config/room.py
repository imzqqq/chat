import logging

from chat.api.constants import RoomCreationPreset

from ._base import Config, ConfigError

logger = logging.Logger(__name__)


class RoomDefaultEncryptionTypes:
    """Possible values for the encryption_enabled_by_default_for_room_type config option"""

    ALL = "all"
    INVITE = "invite"
    OFF = "off"


class RoomConfig(Config):
    section = "room"

    def read_config(self, config, **kwargs):
        # Whether new, locally-created rooms should have encryption enabled
        encryption_for_room_type = config.get(
            "encryption_enabled_by_default_for_room_type",
            RoomDefaultEncryptionTypes.OFF,
        )
        if encryption_for_room_type == RoomDefaultEncryptionTypes.ALL:
            self.encryption_enabled_by_default_for_room_presets = [
                RoomCreationPreset.PRIVATE_CHAT,
                RoomCreationPreset.TRUSTED_PRIVATE_CHAT,
                RoomCreationPreset.PUBLIC_CHAT,
            ]
        elif encryption_for_room_type == RoomDefaultEncryptionTypes.INVITE:
            self.encryption_enabled_by_default_for_room_presets = [
                RoomCreationPreset.PRIVATE_CHAT,
                RoomCreationPreset.TRUSTED_PRIVATE_CHAT,
            ]
        elif (
            encryption_for_room_type == RoomDefaultEncryptionTypes.OFF
            or encryption_for_room_type is False
        ):
            # PyYAML translates "off" into False if it's unquoted, so we also need to
            # check for encryption_for_room_type being False.
            self.encryption_enabled_by_default_for_room_presets = []
        else:
            raise ConfigError(
                "Invalid value for encryption_enabled_by_default_for_room_type"
            )

    def generate_config_section(self, **kwargs):
        return """\
        ## Rooms ##

        # Controls whether locally-created rooms should be end-to-end encrypted by
        # default.
        #
        # Possible options are "all", "invite", and "off". They are defined as:
        #
        # * "all": any locally-created room
        # * "invite": any room created with the "private_chat" or "trusted_private_chat"
        #             room creation presets
        # * "off": this option will take no effect
        #
        # The default value is "off".
        #
        # Note that this option will only affect rooms created after it is set. It
        # will also not affect rooms created by other servers.
        #
        #encryption_enabled_by_default_for_room_type: invite
        """
