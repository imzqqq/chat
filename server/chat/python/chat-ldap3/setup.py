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
    lines = [line for line in code.split('\n') if line.startswith(name)]
    exec("\n".join(lines), result)
    return result[name]


setup(
    name="chat-ldap3",
    keywords="ldap3",
    author="imqzzZ",
    author_email="imzqqq@hotmail.com",
    url="https://github.com/imzqqq",
    version=exec_file(("ldap_auth_provider.py",), "__version__"),
    py_modules=["ldap_auth_provider"],
    description="An LDAP3 auth provider for Chat server",
    install_requires=[
        "Twisted>=15.1.0",
        "ldap3>=2.8",
        "service_identity",
    ],
    test_require=[
        "chat-server",
    ],
    long_description=read_file(("README.md",)),
    classifiers=[
        'Development Status :: 4 - Beta',
        'Programming Language :: Python :: 3 :: Only',
    ],
)
