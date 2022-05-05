Typing
=================================

|Type |Description|
| ----------- | ----------- |
|int| Integer |
|float| Floating point numbers |
|bool| Boolean (subclass of `int`) |
|str| Characters (unicode) |
|bytes| 8-bit characters |
|object| Arbitrary `objects` (public base class) |
|List[str]| `List` of characters |
|Tuple[int, int]| A tuple of two `int` objects |
|Tuple[int, ...]| A tuple of any number of `int` objects |
|Dict[str, int]| A dictionary with `str` as key and `int` as value |
|Iterable[int]| Iterable objects containing int |
|Sequence[bool]| Boolean sequence (read-only) |
|Mapping[str, int]| Mapping from str key to int value (read-only) |
|Any| Dynamically typed values with arbitrary types |
|Union| Joint Type |
|Optional| Parameters can be empty or of an already declared type |
|Mapping| Mapping, which is a generic version of collections.abc.Mapping |
|MutableMapping| Subclasses of the Mapping object, variable |
|Generator| Generator[YieldType、SendType、ReturnType] |
|NoReturn| The function does not return a result |
|Set| Generic type for set, recommended for annotating return types |
|AbstractSet| Generic type for collections.abc.Set, recommended for annotating parameters |
|Sequence| Generic types of collections.abc.Sequence, generalized types of lists, tuples, etc. |
|TypeVar| Customization of variables compatible with specific types |
|Generic| Customizing Generic Types |
|NewType| Declare some types with special meaning |
|Callable| Callable type, Callable[[parameter type], return type] |
