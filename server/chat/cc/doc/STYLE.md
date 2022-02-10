# How to CPP for IRCd


In the post-C++11 world it is time to leave C99+ behind and seriously consider
C++ as C proper. It has been a hard 30 year journey to finally earn that, but
now it is time. This document is the effective style guide for how Charybdis
will integrate -std=gnu++17 and how developers should approach it.


### C++ With Respect For C People

Remember your C heritage. There is nothing wrong with C, it is just incomplete.
There is also no overhead with C++, that is a myth. If you write C code in C++
it will be the same C code. Think about it like this: if C is like a bunch of
macros on assembly, C++ is a bunch of macros on C. This guide will not address
any more myths and for that we refer you [here](https://isocpp.org/blog/2014/12/myths-3).


#### Direct initialization

Use `=` only for assignment to an existing object. *Break your C habit right now.*
Use bracket initialization `{}` of all variables and objects. Fall back to parens `()`
if brackets conflict with an initializer_list constructor (such as with STL containers)
or if absolutely necessary to quash warnings about conversions.

> Quick note to preempt a confusion for C people:
> Initialization in C++ is like C but you don't have to use the `=`.
>
> ```C++
> struct user { const char *nick; };
> struct user you = {"you"};
> user me {"me"};
> ```
>

* Use Allman style for complex/long initialization statements. It's like a function
  returning the value to your new object; it is easier to read than one giant line.

> ```C++
> const auto sum
> {
>     1 + (2 + (3 * 4) + 5) + 6
> };
> ```

* Do not put uninitialized variables at the top of a function and assign them
later.

* Even though C++17 mandates [copy elision](https://en.cppreference.com/w/cpp/language/copy_elision)
this project does not relax its comprehensive use of direct initialization.


#### Use full const correctness

`const` correctness should extend to all variables, pointers, arguments, and
functions- not just "pointed-to" data. If it *can* be `const` then make it
`const` and relax it later if necessary.


#### Use auto

Use `auto` whenever it is possible to use it; specify a type when you must.
If the compiler can't figure out the auto, that's when you indicate the type.


#### RAII will be in full force

All variables, whether they're function-local, class-members, even globals,
must always be under some protection at all times. There must be the
expectation at *absolutely any point* including *between those points*
everything will blow up randomly and the protection will be invoked to back-out
the way you came. That is, essentially, **the juice of why we are here.**

**This is really serious business.** You have to do one thing at a time. When you
move on to the next thing the last thing has to have already fully succeeded
or fully failed. Everything is a **transaction**. Nothing in the future exists.
There is nothing you need from the future to give things a consistent state.

* The program should be effectively reversible -- should be able to "go backwards"
or "unwind" from any point. Think in terms of stacks, not linear procedures.
This means when a variable, or member (a **resource**) first comes into scope,
i.e. it is declared or accessible (**acquired**), it must be **initialized**
to a completely consistent state at that point.

>
> Imagine pulling down a window shade to hide the sun. As you pull down, the canvas
> unrolls from its spool at the top. Your goal is to hook the shade on to the nail
> at the bottom of the window: that is reaching the return statement. If you slip
> and let go, the shade will roll back up into the spool at the top: that is an
> exception.
>
> What you can't do is prepare work on the way down which needs _any_ further pulling
> to be in a consistent state and not leak. You might slip and let go at any time for
> any reason. A `malloc()` on one line and a `free()` following it is an example of
> requiring more pulling.
>
> Indeed slipping and letting go is an accident -- but the point is that *accidents
> happen*. They're not always your fault, and many times are in other parts of the
> code which are outside of your control. This is a good approach for robust and
> durable code over long-lived large-scale projects.


#### Exceptions will be used

Wait, you were trolling "respect for C people" right? **No.** If you viewed
the above section merely through the prism avoiding classic memory leaks, and
can foresee how to now write stackful, reversible, protected programs without
even calling free() or delete: you not only have earned the right, but you
**have** to use exceptions. This is no longer a matter of arguing for or
against `if()` statement clutter and checking return types and passing errors
down the stack.

* Object construction (logic in the initialization list, constructor body, etc)
is actual real program logic. Object construction is not something to just
prepare some memory, like initializing it to zero, leaving an instance
somewhere for further functions to conduct operations on. Your whole program
could be running - the entire universe could be running - in some member
initializer somewhere. The only way to error out of this is to throw, and it
is perfectly legitimate to do so.

* Function bodies and return types should not be concerned with error
handling and passing of such. They only cause and generate the errors.

* Try/catch style note: We specifically discourage naked try/catch blocks.
In other words, **most try-catch blocks are of the
[function-try-catch](http://en.cppreference.com/w/cpp/language/function-try-block)
variety.** The style is simply to piggyback the try/catch where another block
would have been.

> ```C++
> while(foo) try
> {
>     ...
> }
> catch(exception)
> {
> }
> ```

* We extend this demotion style of keywords to `do` as well, which should
  avoid having its own line if possible.

> ```C++
> int x; do
> {
>     ...
> }
> while((x = foo());
> ```


#### Encapsulation will be relaxed

To summarize, most structures will default to being fully public unless there
is a very pressing reason to create a private section. Such a reason is not
"the user *could* break something by touching this," instead it is "the user
*will only ever* break something by touching this."

* Do not use the keyword `class` unless your sole intent is to have the members
immediately following it be private. Using `class` followed by a `public:`
label is nubile.

Note that public interfaces and private implementation patterns are still
widely used and encouraged, even expected, but not purely using the C++
language features. The intent here is to allow hacking on the project to be
easy. We don't want to stifle creativity by getting in the way of developers
implementing new ideas which do things that weren't originally intended.
In practice, interfaces try to expose as much as possible, but require only
a tiny surface by default for actual intended use.


#### Pointers and References

* The `&` or `*` prefixes the variable name; it does not postfix the type.
This is evidenced by comma-delimited declarations. There is only one exception
to this for universal references which is described later.

> ```C++
> int a, &b{a}, *c{&b}, *const d{&b}, *const *const e{&c};
> ```

* Biblical maxim: Use references when you can, pointers when you must.

* Pass arguments by const reference `const foo &bar` preferably, non-const
reference `foo &bar` if you must.

* Use const references even if you're not referring to anything created yet.
const references can construct, contain, and refer to an instance of the type
with all in one magic. This style has no sympathy for erroneously expecting
that a const reference is not a local construction; expert C++ developers
do not make this error. See reasons for using a pointer below.

* Passing by value indicates some kind of need for object construction in
the argument, or that something may be std::move()'ed to and from it. Except
for some common patterns, this is generally suspect.

* Passing to a function with an rvalue reference argument `foo &&bar` indicates
something will be std::move()'ed to it, and ownership is now acquired by that
function.

* In a function with a template `template<class foo>`, an rvalue reference in
the prototype for something in the template `void func(foo &&bar)` is actually
a [universal reference](https://isocpp.org/blog/2012/11/universal-references-in-c11-scott-meyers)
which has some differences from a normal rvalue reference. To make this clear
our style is to move the `&&` like so `void func(foo&& bar)`. This is actually
useful because a variadic template foo `template<class... foo>` will require
the prototype `void func(foo&&... bar)`.

* Passing a pointer, or pointer arguments in general, indicates something may
be null (optional), or to explicitly prevent local const construction which is
a rare reason. Otherwise suspect.

* Avoid using references as object members, you're most likely just limiting
the ability to assign, move, and reuse the object because references cannot be
reseated; then the "~~big three~~" "big five" custom constructors have to be
created and maintained, and it becomes an unnecessary mess.


#### Miscellaneous


* Prefer "locality" rather than "centrality." In other words, we keep things
in as local of a scope or file as possible to where it is used.

* new and delete should rarely if ever be seen. This is more true than ever with
C++14 std::make_unique() and std::make_shared().

* We allow some C-style arrays, especially on the stack, even C99 dynamic sized ones;
there's no problem here, just be responsible.

* `alloca()` will not be used.

* C format strings are still acceptable. This is an IRC project, with heavy
use of strings and complex formats and all the stringencies. We even have
our own custom *protocol safe* format string library, and that should be used
where possible.

* streams and standard streams are generally avoided in this project. We could have
have taken the direction to customize C++'s stream interface to make it
performant, but otherwise the streams are generally slow and heavy. Instead we
chose a more classical approach with format strings and buffers -- but without
sacrificing type safety with our RTTI-based fmt library.

* ~~varargs are still legitimate.~~ There are just many cases when template
varargs, now being available, are a better choice; they can also be inlined.

	* Our template va_rtti is starting to emerge as a suitable replacement
	for any use of varags.

* When using a `switch` over an `enum` type, put what would be the `default` case after/outside
of the `switch` unless the situation specifically calls for one. We use -Wswitch so changes to
the enum will provide a good warning to update any `switch`.

* Prototypes should name their argument variables to make them easier to understand, except if
such a name is redundant because the type carries enough information to make it obvious. In
other words, if you have a prototype like `foo(const std::string &message)` you should name
`message` because std::string is common and *what* the string is for is otherwise opaque.
OTOH, if you have `foo(const options &options, const std::string &message)` one should skip
the name for `options &` as it just adds redundant text to the prototype.


#### Comments

* `/* */` Multi-line comments are not normally used. We reserve this for
debugging and temporary multi-line grey-outs. The goal for rarely using this
is to not impede anybody attempting to refactor or grey-out a large swath of
code.

* `//` Primary developer comment; used even on multiple lines.

* `///` Documentation comment; the same style as the single line comment; the
documentation is applied to code that follows the comment block.

* `///<` Documentation comment; this documents code preceding the comment.

##### Documentation will be pedantic, windy and even patronizing

This is considered a huge anti-pattern in most other contexts where comments
and documentation are minimal, read by experts, end up being misleading, tend
to diverge from their associated code after maintenance, etc. This project is
an exception. Consider two things:

1. This is a free and open source public internet project. The goal here
is to make it easy for many-eyeballs to understand everything. Then,
many-eyeballs can help fix comments which become misleading.

2. Most free and open source public internet projects are written in C
because C++ is complicated with a steep learning curve. It is believed
C++ reduces the amount of many-eyeballs. A huge number of contributions
to these projects come from people with limited experience working on
their "first project."

Therefor, writers of documentation will consider a reader which has
encountered IRCd as their first project, specifically in C++. Patronizing
explanations of common/standard C++ patterns and intricacies can be made.


### Art & Tableaux


* Tab style is **tabs before spaces**. Tabs set an indentation level and
then spaces format things *at that level*. This is one of the hardest styles
to get right and then enforce, but it looks the best for everyone. The point
here is that the tab-width becomes a personal setting -- nobody has to argue
whether it's worth 2 or 4 or 8 spaces... Remember, tabs are never used to
align things that would fall out of alignment if the tab-width changed.

* Only one blank line at a time. While an entire section could be devoted to
*where* to create whitespace, for now, just know to only use a single blank
line to do so. There are ways to cheat. I am a huge fan of whitespace and I
will share some of these ways. For example, a comment block may end in a
line starting with `//` with no text after it. Combined with the allowed
completely blank line after that you now have more whitespace.


## Conventions

These are things you should know when mulling over the code as a whole. Knowing
these things will help you avoid various gotchas and not waste your tim
debugging little surprises. You may or may not agree with some of these
choices (specifically the lack of choices in many cases) but that's why they're
explicitly discussed here. Conventions are not laws: they can be ignored or
overruled on a case basis. One should follow them by default.


#### Null termination

- We don't rely on null terminated strings. We always carry around two points
of data to indicate such vectoring. Ideally this is a pair of pointers
indicating the `begin`/`end` like an STL iterator range. `string_view` et al
and the `buffer::` suite work this way.

- Null terminated strings can still be used and we even still create them in
many places on purpose just because we can.

- Null terminated creations use the BSD `strl*` style and *not* the `strn*`
style. Take note of this. When out of buffer space, such an `strl*` style
will *always* add a null to the end of the buffer. Since we almost always
have vectoring data and don't really need this null, a character of the string
may be lost. This can happen when creating a buffer tight to the length of an
expected string without a `+ 1`. This is actually the foundation of a case
to move *back* to `strn*` style but it's not prudent at this time.

- Anything named `print*` like `print(mutable_buffer, T)` always composes null
terminated output into the buffer. These functions usually return a size_t
which count characters printed *not including null*. They may return a
`string_view`/`const_buffer` of that size (never viewing the null).


#### nothrow is not noexcept

Often a function is overloaded with an std::nothrow_t argument or our
util::nothrow overload template. This means the function **will not throw
a specific exception expected from the overload alternative** (or set of
exceptions, etc). Any exception may still come out of that nothrow overload;
technically including the specific exception if it came from somewhere else!

The `noexcept` specifier is employed to mark non-inline functions which don't
throw; we elide `noexcept` for inline functions which don't throw unless there
is an explicit reason for it. The former provides valuable information to the
compiler affecting optimization around calls where the compiler can't see
the callee's implementation to deduce it doesn't throw.

Most of the project propagates exceptions. Functions that handle their errors
and are expected to return (i.e since they catch `std::exception`), still throw 
special exceptions like `ircd::ctx::terminated`. If the `catch(...)` and
`noexcept` features are used: developers must cooperate by handling ctx
interruptions and propagating terminations.


#### Nothing ticks

The project makes considerable use of userspace threads which may be spawned by
various subsystems to perform tasks: some of those tasks tend to be performed at
intervals or in some cases may require scanning data at an interval (i.e timeout
check). Our style is to not wakeup a context (or similarly queue a callback in
the plain event loop) for an empty dataset. In other words, when there is no
work, the program should be entirely comatose and not woken up by the OS.
For example: if you were to `strace(1)` construct and then pull the network
cable: eventually there would be complete silence.



### Git / Development related

Commits in this project tend to have a `prefix:` like `ircd::m:`. This is
simply an indicator of where the change occurred. If multiple areas of the
project are changed: first determine if the change in each area can stand on
its own and break what you're doing into multiple commits; this is generally
the case when adding a low-level feature to support something built at a higher
level. Otherwise, prefix the commit with the largest/most-fundamental area
being changed.
- Prefixes tend to just be the namespace where the change is occurring.
- Prefixes can be an actual class name if that class has a lot of nested
assets and pretty much acts as a namespace.
- Prefixes for changes in `modules/` where code is not in any namespace tend
to be the path to the module i.e `modules/s_conf:` or `modules/client/sync:`
- Prefixes for other areas of the project can just be the directory like `doc:`
or `tools:` or `README:`

Existing conventions for commit wording are documented here as follows:
Generally after the prefix, the most frequent words a commit start with
are "Add" "Fix" "Move" "Remove" and "Improve" and though it is not
required, if you can classify what you're doing with one of those that
is ideal.
- The use of the word "minor" indicates that no application logic was
affected by a commit: i.e code formatting changes and "minor cleanup" etc.
- The use of the word "various" indicates many not-very-related changes
or very spread-out changes: i.e "various fixes" etc; this tends not to be
something one is proud of using.
- The use of the word "checkpoint" indicates something sloppy and
incomplete is being committed; it compiles and runs; there is a pressing
need to get it out of the dirty head for the time being.
