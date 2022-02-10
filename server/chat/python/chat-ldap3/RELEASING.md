# How to release `ldap3`

Releasing `ldap3` involves bumping the version number, creating a new tag on Github, then uploading release packages to [PyPi](https://pypi.org).

1. Edit the `__version__` variable of `ldap_auth_provider.py` to the new release
version. This repository uses [Semantic Versioning](https://semver.org/).

1. Set a variable to the version number for convenience:

   ```sh
   ver=$(python3 -c 'import ldap_auth_provider; print(ldap_auth_provider.__version__)')
   ```

1. Push your changes:

   ```sh
   git add -u && git commit -m $ver && git push
   ```

1. Create a signed git tag for the release:

   ```sh
   git tag -s v$ver
   ```

   Set the first line of the message to `vX.Y.Z`, and the rest to the changes since the last release (hint: `git log --pretty=%s --reverse v<old ver>...`)

1. Push the tag:

   ```sh
   git push origin tag v$ver
   ```

1. Build and upload to PyPI:

   ```sh
   python setup.py sdist
   twine upload dist/chat-ldap3-$ver.tar.gz
   ```

1. Create release on GH project page:

   ```sh
   xdg-open https://github.com/matrix-org/chat-ldap3/releases/edit/v$ver
   ```
