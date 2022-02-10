from frozendict import frozendict


def freeze(o):
    if isinstance(o, dict):
        return frozendict({k: freeze(v) for k, v in o.items()})

    if isinstance(o, frozendict):
        return o

    if isinstance(o, (bytes, str)):
        return o

    try:
        return tuple(freeze(i) for i in o)
    except TypeError:
        pass

    return o


def unfreeze(o):
    if isinstance(o, (dict, frozendict)):
        return {k: unfreeze(v) for k, v in o.items()}

    if isinstance(o, (bytes, str)):
        return o

    try:
        return [unfreeze(i) for i in o]
    except TypeError:
        pass

    return o
