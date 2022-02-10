#!/usr/bin/env python

import glob
import os

from setuptools import Command, find_packages, setup

here = os.path.abspath(os.path.dirname(__file__))


# Some notes on `setup.py test`:
#
# Once upon a time we used to try to make `setup.py test` run `tox` to run the
# tests. That's a bad idea for three reasons:
#
# 1: `setup.py test` is supposed to find out whether the tests work in the
#    *current* environment, not whatever tox sets up.
# 2: Empirically, trying to install tox during the test run wasn't working ("No
#    module named virtualenv").
# 3: The tox documentation advises against it[1].
#
# Even further back in time, we used to use setuptools_trial [2]. That has its
# own set of issues: for instance, it requires installation of Twisted to build
# an sdist (because the recommended mode of usage is to add it to
# `setup_requires`). That in turn means that in order to successfully run tox
# you have to have the python header files installed for whichever version of
# python tox uses (which is python3 on recent ubuntus, for example).
#
# So, for now at least, we stick with what appears to be the convention among
# Twisted projects, and don't attempt to do anything when someone runs
# `setup.py test`; instead we direct people to run `trial` directly if they
# care.
#
# [1]: http://tox.readthedocs.io/en/2.5.0/example/basic.html#integration-with-setup-py-test-command
# [2]: https://pypi.python.org/pypi/setuptools_trial
class TestCommand(Command):
    user_options = []

    def initialize_options(self):
        pass

    def finalize_options(self):
        pass

    def run(self):
        print(
            """Chat server's tests cannot be run via setup.py. To run them, try:
     PYTHONPATH="." trial tests
"""
        )


def read_file(path_segments):
    """Read a file from the package. Takes a list of strings to join to
    make the path"""
    file_path = os.path.join(here, *path_segments)
    with open(file_path) as f:
        return f.read()


def exec_file(path_segments):
    """Execute a single python file to get the variables defined in it"""
    result = {}
    code = read_file(path_segments)
    exec(code, result)
    return result


version = exec_file(("chat", "__init__.py"))["__version__"]
dependencies = exec_file(("chat", "python_dependencies.py"))
### MARK
long_description = read_file(("README.md",))
###

REQUIREMENTS = dependencies["REQUIREMENTS"]
CONDITIONAL_REQUIREMENTS = dependencies["CONDITIONAL_REQUIREMENTS"]
ALL_OPTIONAL_REQUIREMENTS = dependencies["ALL_OPTIONAL_REQUIREMENTS"]

# Make `pip install chat-server[all]` install all the optional dependencies.
CONDITIONAL_REQUIREMENTS["all"] = list(ALL_OPTIONAL_REQUIREMENTS)

# Developer dependencies should not get included in "all".
#
# We pin black so that our tests don't start failing on new releases.
CONDITIONAL_REQUIREMENTS["lint"] = [
    "isort==5.7.0",
    "black==21.6b0",
    "flake8-comprehensions",
    "flake8-bugbear==21.3.2",
    "flake8",
]

CONDITIONAL_REQUIREMENTS["dev"] = CONDITIONAL_REQUIREMENTS["lint"] + [
    # The following are used by the release script
    "click==7.1.2",
    "redbaron==0.9.2",
    "GitPython==3.1.14",
    "commonmark==0.9.1",
    "pygithub==1.55",
]

CONDITIONAL_REQUIREMENTS["mypy"] = ["mypy==0.812", "mypy-zope==0.2.13"]

# Dependencies which are exclusively required by unit test code. This is
# NOT a list of all modules that are necessary to run the unit tests.
# Tests assume that all optional dependencies are installed.
#
# parameterized_class decorator was introduced in parameterized 0.7.0
CONDITIONAL_REQUIREMENTS["test"] = ["parameterized>=0.7.0"]

setup(
    name="chat-server",
    version=version,
    packages=find_packages(exclude=["tests", "tests.*"]),
    description="Reference homeserver for the decentralized comms protocol",
    install_requires=REQUIREMENTS,
    extras_require=CONDITIONAL_REQUIREMENTS,
    include_package_data=True,
    zip_safe=False,
    long_description=long_description,
    long_description_content_type="text/x-rst",
    python_requires="~=3.6",
    classifiers=[
        "Development Status :: 5 - Production/Stable",
        "Topic :: Communications :: Chat",
        "Programming Language :: Python :: 3 :: Only",
        "Programming Language :: Python :: 3.6",
        "Programming Language :: Python :: 3.7",
        "Programming Language :: Python :: 3.8",
        "Programming Language :: Python :: 3.9",
    ],
    scripts=["synctl"] + glob.glob("scripts/*"),
    cmdclass={"test": TestCommand},
)
