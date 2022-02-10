#!/usr/bin/env python

from setuptools import setup
from codecs import open
import os

here = os.path.abspath(os.path.dirname(__file__))


def read_file(path_segments):
    """Read a UTF-8 file from the package. Takes a list of strings to join to
    make the path"""
    file_path = os.path.join(here, *path_segments)
    with open(file_path, encoding="utf-8") as f:
        return f.read()


def exec_file(path_segments, name):
    """Extract a constant from a python file by looking for a line defining
    the constant and executing it."""
    result = {}
    code = read_file(path_segments)
    lines = [line for line in code.split("\n") if line.startswith(name)]
    exec("\n".join(lines), result)
    return result[name]


setup(
    name="canonicaljson",
    version=exec_file(("canonicaljson.py",), "__version__"),
    py_modules=["canonicaljson"],
    description="Canonical JSON",
    install_requires=[
        # simplejson versions before 3.14.0 had a bug with some characters
        # (e.g. \u2028) if ensure_ascii was set to false.
        "simplejson>=3.14.0",
        "frozendict>=1.0",
    ],
    zip_safe=True,
    long_description=read_file(("README.md",)),
    keywords="json",
    author="imqzzZ",
    author_email="imzqqq@hotmail.com",
    url="https://github.com/imzqqq",
    python_requires="~=3.5",
    classifiers=[
        "Development Status :: 5 - Production/Stable",
        "Intended Audience :: Developers",
        "Programming Language :: Python :: 2",
        "Programming Language :: Python :: 3",
    ],
)
