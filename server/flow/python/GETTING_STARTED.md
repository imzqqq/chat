# Flow

## Getting started

### Install deps

Install package manager poetry:
`pipx install poetry`

Install all deps:
`poetry env use "3.10"` (optional)
`poetry install --no-interaction`

### Generate config

`poetry run python config_wizard.py`

### Start server

`poetry run inv uvicorn`
