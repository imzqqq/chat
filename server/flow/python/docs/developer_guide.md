# Developer's guide

This guide assume you have some knoweldge of [ActivityPub](https://activitypub.rocks/).

[TOC]

## Architecture

Microblog.pub is a "modern" Python application with "old-school" server-rendered templates.

 - [Poetry](https://python-poetry.org/) is used for dependency management.
 - Most of the code is asynchronous, using [asyncio](https://docs.python.org/3/library/asyncio.html).
 - SQLite3 is the default database.

The server has 2 components:

 - The web server (powered by [FastAPI](https://fastapi.tiangolo.com/) and [Jinja2](https://jinja.palletsprojects.com/en/3.1.x/) templates)
 - An additional process that takes care of sending "outgoing actities" 

### Tasks

The project uses [Invoke](https://www.pyinvoke.org/) to manage tasks (a Python powered Makefile).

You can find the tasks definition in `tasks.py` and list the tasks using:

```bash
inv -l
```

### Media storage

The uploads are stored in the `data/` directory, using a simple content-addressed storage (file contents hash is the name of the store BLOB).
Files metadata are stored in the database.

## Installation

Running a local version requires:

 - Python 3.10+
 - SQLite 3.35+

You can follow the [Python developer version of the install instructions](https://docs.microblog.pub/installing.html#python-developer-edition).

## Documentation

The documention is managed as Markdown files in `docs/` and the online documentation is built using a homegrown Python script (`scripts/build_docs.py`).

You can build the documentation locally by running:

```bash
inv build-docs
```

And check out the result by starting a static server using Python standard library:

```bash
cd docs/dist
python -m http.server 8001
```
