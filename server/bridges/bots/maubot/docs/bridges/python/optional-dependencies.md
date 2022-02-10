# Optional dependencies

## Usage

### Production setup
The pip install URLs in the production setup guide include `[all]` at the end 
by default, which means all optional dependencies will be installed by default.

If you only want specific optional dependencies, replace the `all` with a 
comma-separated list of the pip extra names (e.g. `postgres,speedups`).

If you don't want any optional dependencies, just remove the `[all]`.

### Development setup
To install all optional dependencies, use `pip install --upgrade -r optional-requirements.txt`.

To install specific optional dependencies, install the packages listed 
"Required packages" from the table of optional dependencies below. You can also 
check the expected versions of the packages from `optional-requirements.txt`.

### Docker
The docker images contain all optional dependencies. Currently they can't be 
easily disabled.

## List of optional dependencies
The `†` symbol means you must also enable the feature in the config. Required 
packages in parentheses indicate a large dependency of the other packages.

### All Python bridges
| pip extra name      | Required packages | Description                                                        |
|---------------------|-------------------|--------------------------------------------------------------------|
| †`metrics`          | prometheus_client | Prometheus metrics.                                                |
| †`e2be`             | asyncpg<br>python-olm<br>pycryptodome<br>unpaddedbase64 | End-to-bridge encryption support (see native dependency below). |

**N.B.** python-olm requires libolm3 with dev headers, Python dev headers, and
a C compiler. This means `libolm-dev`, `python3-dev` and `build-essential` on
Debian 11+ and Ubuntu 19.10+. On  older Debian-based distros, install
`libolm-dev` from backports.

If you want to avoid the dev headers, you can install the libolm3 package
without -dev and get a pre-compiled python-olm from gitlab.matrix.org's
PyPI registry:

```shell
pip install python-olm --extra-index-url https://gitlab.matrix.org/api/v4/projects/27/packages/pypi/simple
```

### mautrix-telegram
| pip extra name      | Required packages | Description                                                        |
|---------------------|-------------------|--------------------------------------------------------------------|
| `speedups`          | cryptg<br>cchardet<br>aiodns<br>brotli | Speed up some things, e.g. by using native crypto code. |
| `hq_thumbnails`     | moviepy (numpy)   | High quality thumbnails for Telegram->Matrix gifs and videos.  If you want to use an existing `ffmpeg` installation for `moviepy`, set the `FFMPEG_BINARY` environment variable before starting the bridge. If it's not set, a ffmpeg binary will be downloaded automatically. |
| `qr_login`          | qrcode<br>Pillow  | Telegram login by scanning a QR code from another device.          |
| †`postgres`         | psycopg2          | Postgres database support                                          |

### mautrix-facebook
| pip extra name      | Required packages | Description                                                        |
|---------------------|-------------------|--------------------------------------------------------------------|
| `animated_stickers` | Pillow            | Finds the dimensions of stickers bridged from Facebook.            |

### mautrix-instagram
| pip extra name      | Required packages | Description                                                          |
|---------------------|-------------------|----------------------------------------------------------------------|
| `imageconvert`      | Pillow            | Convert images from Matrix into JPEG so Instagram would accept them. |

### mautrix-signal
| pip extra name      | Required packages | Description                                                          |
|---------------------|-------------------|----------------------------------------------------------------------|
| `formattednumbers`  | phonenumbers      | Format phone numbers nicely before using as displaynames.            |
| `qrlink`            | qrcode<br>Pillow  | Generate QR codes required for linking as a secondary device.        |
| `stickers`          | signalstickers-client | Enable bridging of Signal stickers to Matrix.                    |
