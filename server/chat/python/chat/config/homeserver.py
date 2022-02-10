from ._base import RootConfig
from .account_validity import AccountValidityConfig
from .api import ApiConfig
from .appservice import AppServiceConfig
from .auth import AuthConfig
from .cache import CacheConfig
from .captcha import CaptchaConfig
from .cas import CasConfig
from .consent import ConsentConfig
from .database import DatabaseConfig
from .emailconfig import EmailConfig
from .experimental import ExperimentalConfig
from .federation import FederationConfig
from .groups import GroupsConfig
from .jwt import JWTConfig
from .key import KeyConfig
from .logger import LoggingConfig
from .metrics import MetricsConfig
from .modules import ModulesConfig
from .oidc import OIDCConfig
from .password_auth_providers import PasswordAuthProviderConfig
from .push import PushConfig
from .ratelimiting import RatelimitConfig
from .redis import RedisConfig
from .registration import RegistrationConfig
from .repository import ContentRepositoryConfig
from .room import RoomConfig
from .room_directory import RoomDirectoryConfig
from .saml2 import SAML2Config
from .server import ServerConfig
from .server_notices import ServerNoticesConfig
from .spam_checker import SpamCheckerConfig
from .sso import SSOConfig
from .stats import StatsConfig
from .third_party_event_rules import ThirdPartyRulesConfig
from .tls import TlsConfig
from .tracer import TracerConfig
from .user_directory import UserDirectoryConfig
from .voip import VoipConfig
from .workers import WorkerConfig


class HomeServerConfig(RootConfig):

    config_classes = [
        ModulesConfig,
        ServerConfig,
        TlsConfig,
        FederationConfig,
        CacheConfig,
        DatabaseConfig,
        LoggingConfig,
        RatelimitConfig,
        ContentRepositoryConfig,
        CaptchaConfig,
        VoipConfig,
        RegistrationConfig,
        AccountValidityConfig,
        MetricsConfig,
        ApiConfig,
        AppServiceConfig,
        KeyConfig,
        SAML2Config,
        OIDCConfig,
        CasConfig,
        SSOConfig,
        JWTConfig,
        AuthConfig,
        EmailConfig,
        PasswordAuthProviderConfig,
        PushConfig,
        SpamCheckerConfig,
        RoomConfig,
        GroupsConfig,
        UserDirectoryConfig,
        ConsentConfig,
        StatsConfig,
        ServerNoticesConfig,
        RoomDirectoryConfig,
        ThirdPartyRulesConfig,
        TracerConfig,
        WorkerConfig,
        RedisConfig,
        ExperimentalConfig,
    ]
