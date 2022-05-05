# mautrix-docs

This repo contains the documentation for all my Matrix software. The intention
is to reduce duplication that was present in the old bridge wikis.

The books are built with [mdBook](https://github.com/rust-lang/mdBook).
The front page in the `bookshelf` directory is based on
[docs.t2bot.io](https://github.com/t2bot/docs.t2bot.io).

## Getting started

- Run dev: `mdbook serve path/to/book -p 8000 -n 127.0.0.1`
- Build: `mdbook build`
