# BUILD

1. **Fetch dependencies**

    - Ubuntu Cosmic (18.10) through Focal (20.04):
    ```autoconf autoconf-archive autoconf2.13 automake autotools-dev libboost1.71-all-dev build-essential git libbz2-dev libmagic-dev libnss-db libsodium-dev libssl-dev libtool shtool xz-utils libgraphicsmagick1-dev libgraphicsmagick-q16-3 libpng-dev libicu-dev libjemalloc-dev liblz4-dev libzstd-dev librocksdb-dev clang llvm-dev```

    ❗ THE COMPLETE SOURCE-CODE OF ROCKSDB MUST BE AVAILABLE TO BUILD CONSTRUCT. This is different from the `include/` and `lib/` files installed by your distribution's package system. Most platforms do not have to build the source, but it must be available.

    ```
    git submodule update --init deps/rocksdb
    cd deps/rocksdb
    git fetch --tags --force
    git checkout v5.17.2
    ```
    > 👉 For best performance and stability, please check for the version available on
your system and match that with the `git checkout` command above.

    > 🛑 RocksDB 6.10.X and 6.11.X have a bug and should not be used. If you are affected by these versions please add `--with-included-rocksdb=v6.13.3` to `./configure` when instructed below.


2. **Review special requirements for your platform**

    - #### Arch Linux
        RocksDB versions 6.10.X and 6.11.X should not be used. If the RocksDB package version falls within this range (i.e. 6.10.2-1) you must configure with `--with-included-rocksdb=v6.12.7` or later.

    - #### Ubuntu
        You must configure Construct with the option `--with-included-rocksdb`. This will fetch and properly build rocksdb.
        > Ubuntu builds their library with `-Bsymbolic-functions`. This conflicts with
the requirements of Construct's embedding.

    - ##### Ubuntu Bionic (18.04) and earlier
        Remove `boost1.71` from the package list in the earlier section; add `--with-included-boost` to `./configure` when instructed below.


3. **Review the installation layout**

    At this time it is suggested to supply `./configure` with a `--prefix` path,
especially for development. Example `--prefix=$HOME/.local/`.

    - Binary executable `$prefix/bin/construct`
    - Shared library `$prefix/lib/libircd.so`
    - Shared library modules `$prefix/lib/modules/construct/*.so`
    - Header files `$prefix/include/ircd/*`
    - Read-only shared assets `$prefix/share/construct/*`
    - Database directory may be established at `$prefix/var/db/construct/`

4. **Build Construct**

    > ❗ Any `--with-included-*` option to configure will fetch, configure **and build** the dependencies included as submodules. The result cannot be installed on the system without this repository remaining intact. Please review the special requirements first to understand which options you need or don't need on your system.

    > 👉 Do not set your `--prefix` path to a directory inside your git repository or an invocation of `git clean` will erase your database in $prefix/var/db/.

    ```
    export CXX=clang++
    export CC=clang

    ./autogen.sh
    ./configure --prefix=$HOME/construct_sysroot
    make install
    ```


##### REBUILDING FOR UPDATES

Until [#84](https://github.com/matrix-construct/construct/issues/84) is resolved, updates to the repository (from e.g. `git pull`) or the system may cause complications with incremental builds using `make(1)`. For this reason we advise a "from scratch" build preceded by an invocation of `git clean`; example:

```
git reset --hard  &&  git clean -f -x -d
./autogen.sh
./configure --prefix=...
```

#### DEVELOPMENT BUILDS

Those wishing to contribute new functionality or diagnose difficult bugs will benefit from several configuration options. This configuration generates a significant amount of additional code and instrumentation, in addition to full debug logging. The server may consume additional CPU and memory resources.

> ❗ Until the resolution of [#84](https://github.com/matrix-construct/construct/issues/84) it is advised to sterilize the build environment before any re-`./configure`.

> All `--with-included-` and other platform-specific build options remain the same and must be applied to these recipes.

```
./configure --prefix=... --enable-debug --enable-optimize --with-assert=trap --disable-lto
```
- Release optimization is always enabled; we only advise the removal of `--enable-optimize` when it is necessary to diagnose specific difficult issues.
- It is much easier to debug, step, and fiddle with a live program rather than a dead one. `--with-assert=trap` gives the developer a choice to note the issue and continue the program at their discretion (If you aren't sure about an assert: terminate immediately!).
- Disabling LTO is advised to reduce significant delays during the linking phase of every `make` during development.


## Additional Build Options

#### Debug mode

```
--enable-debug
```
Full debug mode. Includes additional code within `#ifdef RB_DEBUG` sections.
Optimization level is `-Og`, which is still valgrind-worthy. Debugger support
is `-ggdb`. Log level is `DEBUG` (maximum). Assertions are enabled. No
sanitizer instrumentation is generated by default in this mode.


#### Generic mode binary (for distribution packages)

Construct developers have set the default compilation to generate native
hardware operations which may only be supported on very specific targets. For
a generic mode binary, package maintainers may require this option.

```
--enable-generic
```
Sets `-mtune=generic` as `native` is otherwise the default.


#### Compact mode (experimental)

```
--enable-compact
```
Create the smallest possible resulting output. This will optimize for size
(if optimization is enabled), remove all debugging, strip symbols, and apply
any toolchain-feature or #ifdef in code that optimizes the output size.

_This feature is experimental. It may not build or execute on all platforms
reliably. Please report bugs._


#### Manually enable assertions

```
--enable-assert
```
Implied by `--enable-debug`. This is useful to specifically enable `assert()`
statements when `--enable-debug` is not used.

```
--with-assert=trap
```
Recommended when using `--enable-assert` for debugging. This replaces the
default mechanism of assertion with traps rather than aborts; allowing
developers to explore an unterminated program.

#### Manually enable optimization

```
--enable-optimize
```
This manually applies full release-mode optimizations even when using
`--enable-debug`. Implied when not in debug mode.

#### Disable link-time optimization (LTO)

```
--disable-lto
```
LTO is enabled when optimization is enabled (and when the toolchain supports it). When developing in optimized mode, disabling LTO is strongly advised to reduce excessive link times.

#### Disable third-party dynamic allocator libraries

```
--disable-malloc-libs
```
`./configure` will detect alternative `malloc()` implementations found in
libraries installed on the system (jemalloc/tcmalloc/etc). Construct developers
may enable these to be configured by default, if detected. To always prevent
any alternative to the default standard library allocator specify this option.


#### Enable third-party dynamic allocator libraries

Currently:
```
--disable-jemalloc
```

`./configure` will detect alternative `malloc()` implementations found in
libraries installed on the system (jemalloc/tcmalloc/etc). These are recommended for best performance.


#### Logging level

```
--with-log-level=
```
This manually sets the level of logging. All log levels at or below this level
will be available. When a log level is not available, all code used to generate
its messages will be entirely eliminated via *dead-code-elimination* at compile
time.

The log levels are (from logger.h):
```
7  DEBUG      Maximum verbosity for developers.
6  DWARNING   A warning but only for developers (more frequent than WARNING).
5  DERROR     An error but only worthy of developers (more frequent than ERROR).
4  INFO       A more frequent message with good news.
3  NOTICE     An infrequent important message with neutral or positive news.
2  WARNING    Non-impacting undesirable behavior user should know about.
1  ERROR      Things that shouldn't happen; user impacted and should know.
0  CRITICAL   Catastrophic/unrecoverable; program is in a compromised state.
```

When `--enable-debug` is used `--with-log-level=DEBUG` is implied. Otherwise
for release mode `--with-log-level=INFO` is implied. Large deployments with
many users may consider lower than `INFO` to maximize optimization and reduce
noise.
