"""This is a mypy plugin for Chat server to deal with some of the funky typing that
can crop up, e.g the cache descriptors.
"""

from typing import Callable, Optional

from mypy.nodes import ARG_NAMED_OPT
from mypy.plugin import MethodSigContext, Plugin
from mypy.typeops import bind_self
from mypy.types import CallableType, NoneType


class SynapsePlugin(Plugin):
    def get_method_signature_hook(
        self, fullname: str
    ) -> Optional[Callable[[MethodSigContext], CallableType]]:
        if fullname.startswith(
            "chat.util.caches.descriptors._CachedFunction.__call__"
        ) or fullname.startswith(
            "chat.util.caches.descriptors._LruCachedFunction.__call__"
        ):
            return cached_function_method_signature
        return None


def cached_function_method_signature(ctx: MethodSigContext) -> CallableType:
    """Fixes the `_CachedFunction.__call__` signature to be correct.

    It already has *almost* the correct signature, except:

        1. the `self` argument needs to be marked as "bound";
        2. any `cache_context` argument should be removed;
        3. an optional keyword argument `on_invalidated` should be added.
    """

    # First we mark this as a bound function signature.
    signature = bind_self(ctx.default_signature)

    # Secondly, we remove any "cache_context" args.
    #
    # Note: We should be only doing this if `cache_context=True` is set, but if
    # it isn't then the code will raise an exception when its called anyway, so
    # its not the end of the world.
    context_arg_index = None
    for idx, name in enumerate(signature.arg_names):
        if name == "cache_context":
            context_arg_index = idx
            break

    arg_types = list(signature.arg_types)
    arg_names = list(signature.arg_names)
    arg_kinds = list(signature.arg_kinds)

    if context_arg_index:
        arg_types.pop(context_arg_index)
        arg_names.pop(context_arg_index)
        arg_kinds.pop(context_arg_index)

    # Third, we add an optional "on_invalidate" argument.
    #
    # This is a callable which accepts no input and returns nothing.
    calltyp = CallableType(
        arg_types=[],
        arg_kinds=[],
        arg_names=[],
        ret_type=NoneType(),
        fallback=ctx.api.named_generic_type("builtins.function", []),
    )

    arg_types.append(calltyp)
    arg_names.append("on_invalidate")
    arg_kinds.append(ARG_NAMED_OPT)  # Arg is an optional kwarg.

    signature = signature.copy_modified(
        arg_types=arg_types,
        arg_names=arg_names,
        arg_kinds=arg_kinds,
    )

    return signature


def plugin(version: str):
    # This is the entry point of the plugin, and let's us deal with the fact
    # that the mypy plugin interface is *not* stable by looking at the version
    # string.
    #
    # However, since we pin the version of mypy Chat server uses in CI, we don't
    # really care.
    return SynapsePlugin
