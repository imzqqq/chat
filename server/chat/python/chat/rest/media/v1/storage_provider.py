import abc
import logging
import os
import shutil
from typing import TYPE_CHECKING, Optional

from chat.config._base import Config
from chat.logging.context import defer_to_thread, run_in_background
from chat.util.async_helpers import maybe_awaitable

from ._base import FileInfo, Responder
from .media_storage import FileResponder

logger = logging.getLogger(__name__)

if TYPE_CHECKING:
    from chat.server import HomeServer


class StorageProvider(metaclass=abc.ABCMeta):
    """A storage provider is a service that can store uploaded media and
    retrieve them.
    """

    @abc.abstractmethod
    async def store_file(self, path: str, file_info: FileInfo) -> None:
        """Store the file described by file_info. The actual contents can be
        retrieved by reading the file in file_info.upload_path.

        Args:
            path: Relative path of file in local cache
            file_info: The metadata of the file.
        """

    @abc.abstractmethod
    async def fetch(self, path: str, file_info: FileInfo) -> Optional[Responder]:
        """Attempt to fetch the file described by file_info and stream it
        into writer.

        Args:
            path: Relative path of file in local cache
            file_info: The metadata of the file.

        Returns:
            Returns a Responder if the provider has the file, otherwise returns None.
        """


class StorageProviderWrapper(StorageProvider):
    """Wraps a storage provider and provides various config options

    Args:
        backend: The storage provider to wrap.
        store_local: Whether to store new local files or not.
        store_synchronous: Whether to wait for file to be successfully
            uploaded, or todo the upload in the background.
        store_remote: Whether remote media should be uploaded
    """

    def __init__(
        self,
        backend: StorageProvider,
        store_local: bool,
        store_synchronous: bool,
        store_remote: bool,
    ):
        self.backend = backend
        self.store_local = store_local
        self.store_synchronous = store_synchronous
        self.store_remote = store_remote

    def __str__(self) -> str:
        return "StorageProviderWrapper[%s]" % (self.backend,)

    async def store_file(self, path: str, file_info: FileInfo) -> None:
        if not file_info.server_name and not self.store_local:
            return None

        if file_info.server_name and not self.store_remote:
            return None

        if self.store_synchronous:
            # store_file is supposed to return an Awaitable, but guard
            # against improper implementations.
            await maybe_awaitable(self.backend.store_file(path, file_info))  # type: ignore
        else:
            # TODO: Handle errors.
            async def store():
                try:
                    return await maybe_awaitable(
                        self.backend.store_file(path, file_info)
                    )
                except Exception:
                    logger.exception("Error storing file")

            run_in_background(store)

    async def fetch(self, path: str, file_info: FileInfo) -> Optional[Responder]:
        # store_file is supposed to return an Awaitable, but guard
        # against improper implementations.
        return await maybe_awaitable(self.backend.fetch(path, file_info))


class FileStorageProviderBackend(StorageProvider):
    """A storage provider that stores files in a directory on a filesystem.

    Args:
        hs
        config: The config returned by `parse_config`.
    """

    def __init__(self, hs: "HomeServer", config: str):
        self.hs = hs
        self.cache_directory = hs.config.media_store_path
        self.base_directory = config

    def __str__(self):
        return "FileStorageProviderBackend[%s]" % (self.base_directory,)

    async def store_file(self, path: str, file_info: FileInfo) -> None:
        """See StorageProvider.store_file"""

        primary_fname = os.path.join(self.cache_directory, path)
        backup_fname = os.path.join(self.base_directory, path)

        dirname = os.path.dirname(backup_fname)
        if not os.path.exists(dirname):
            os.makedirs(dirname)

        await defer_to_thread(
            self.hs.get_reactor(), shutil.copyfile, primary_fname, backup_fname
        )

    async def fetch(self, path: str, file_info: FileInfo) -> Optional[Responder]:
        """See StorageProvider.fetch"""

        backup_fname = os.path.join(self.base_directory, path)
        if os.path.isfile(backup_fname):
            return FileResponder(open(backup_fname, "rb"))

        return None

    @staticmethod
    def parse_config(config: dict) -> str:
        """Called on startup to parse config supplied. This should parse
        the config and raise if there is a problem.

        The returned value is passed into the constructor.

        In this case we only care about a single param, the directory, so let's
        just pull that out.
        """
        return Config.ensure_directory(config["directory"])
