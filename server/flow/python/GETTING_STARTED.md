# Flow

## Getting started

### Install deps

Install package manager poetry:
`pip install poetry`

Install all deps:
`poetry env use PYTHONPATH` (optional)
`poetry shell`
`poetry env info`
`source poetry_env_path/Scripts/activate`

`poetry install --no-interaction`

### Generate config

`export PYTHONPATH=$PWD`
`poetry run python config_wizard.py`

### Prepare database

`poetry run python app/database.py`

### Start server

`poetry run inv uvicorn`
