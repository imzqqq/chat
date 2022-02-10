# Chat Web/Desktop

The most important changes are:

- A unifed chat list for both direct and group chats
- Message bubbles
- Bigger items in the room list
- &hellip; and more!

## Building Web/Desktop

This particular repo is a wrapper project for element-desktop, element-web, matrix-react-sdk and matrix-js-sdk. It's the recommended starting point to build Chat for Web **and** Desktop.

<pre><code><b>desktop</b> <i>&lt;-- this repo</i> (recommended starting point to build Chat for Web <b>and</b> Desktop)
|-- <a href="./element-desktop">element-desktop</a> (electron wrapper)
|-- <a href="./element-web">element-web</a> ("skin" for matrix-react-sdk)
|-- <a href="./matrix-react-sdk">matrix-react-sdk</a> (most of the development happens here)
`-- <a href="./matrix-js-sdk">matrix-js-sdk</a> (Chat client js sdk)
</code></pre>

### Install dependencies

#### Debian build dependencies

Since Debian is usually slow to update packages on its stable releases,
some dependencies might not be recent enough to build Chat.  
The following are the dependencies required to build Chat Web/Desktop on Debian 10:

```bash
# 1. FIXME: bsdtar cannot be installed
apt install bsdtar
apt install vim curl git make gcc g++ libsqlcipher-dev pkg-config libsecret-1-dev

# 2.
curl -sL https://deb.nodesource.com/setup_14.x | bash -
apt update
apt install nodejs

# 3.
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list
apt update
apt install yarn

# 4.Install rust officially
#changing permissions of your cargo home dir. i.e
sudo chown -R imzqqq /Users/imzqqq/.cargo/
sudo chown -R imzqqq /Users/imzqqq/.rustup/

curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh
echo 'export PATH="$PATH:$HOME/.cargo/bin"' >> .bashrc
source .bashrc

# or install rust by mirrors.tuna.tsinghua
RUSTUP_DIST_SERVER=https://mirrors.tuna.tsinghua.edu.cn/rustup rustup install stable

# 5.Wine for build window release on linux
# TODO: docker

# or use this way
# If your system is 64 bit, enable 32 bit architecture (if you haven't already):
sudo dpkg --add-architecture i386 
# Download and add the repository key:
wget -nc https://dl.winehq.org/wine-builds/winehq.key
sudo apt-key add winehq.key
sudo add-apt-repository 'deb https://dl.winehq.org/wine-builds/ubuntu/ bionic main'
# Update packages:
sudo apt update
# Install stable branch
sudo apt install --install-recommends winehq-stable
# To build app in 32 bit from a machine with 64 bit:
sudo apt-get install --no-install-recommends -y gcc-multilib g++-multilib
# To build app in distributable format for Linux:
sudo apt-get install --no-install-recommends -y libopenjp2-tools
```

#### macOS build dependencies

##### Install brew package manager

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

##### Install packages

```bash
# FIXME: this cannot be installed
brew install tcl
brew install rust node gpg vim curl git yarn git make gcc
brew install sqlcipher
```

#### Signed macOS builds

To sign a macOS build set the environment or make variable `CSC_IDENTITY_AUTO_DISCOVERY` to true
or set `CSC_NAME` to your certificate name or id.

To notarize a build with Apple set `NOTARIZE_APPLE_ID` to your AppleID and set the keychain item
`NOTARIZE_CREDS` to an App specific AppleID password.  

### Initial setup

```bash
make setup
```

### Create release builds

Those are the builds distributed via GitHub releases.

```bash
# The single make targets are explained below
make [{web|debian|windows-setup|windows-portable|macos|macos-mas}-release]
```

After that these packages which belong to to their respective make target should appear in release/\<version\>/:

- `web`: _chat-web-\<version\>.tar.gz_: archive that can be unpacked and served by a **web** server (copy `config.sample.json` to `config.json` and adjust the [configuration](./element-web/docs/config.md) to your likings)
- `debian`: file ready for installation on a **Debian Linux** (based) system via `dpkg -i chat-desktop_<version>_amd64.deb`
- `windows-setup`: _Chat_Setup_v\<version\>.exe_: file ready for **installation** on a **Windows** system
- `windows-portable`: _Chat_win-portable_v\<version\>.zip_: **portable** version for a **Windows** system – take Chat together with your login data around with you (the archive contains a readme with **instructions** and **notes**)
- `macos`: Build a *.dmg for macOS
- `macos-mas`: Build a *.pkg for release in the Mac App Store

#### Additional make targets not used for GitHub releases

- `pacman`: file ready for installation on an **Arch Linux** (based) system via `pacman -U chat-desktop-<version>.pacman`
- `windows-unpacked`: _Chat_win-unpacked_v\<version\>.zip_: **unpacked** archive for a **Windows** system

### Build Web and deploy it directly to your web server

Put the `config.json` with the [configuration](./element-web/docs/config.md) you want for your hosted instance in a subfolder of the `configs` folder.  
Then create a file named `release.mk` and fill it similar to that:

```bash
.PHONY: your-deploy-web

YOUR_CFGDIR := configs/your_subfolder
your-deploy-%: CFGDIR := $(YOUR_CFGDIR)

your-deploy-web: web
 rsync --info=progress2 -rup --del element-web/webapp/ you@yourwebserver:/the/folder/served/for/chat/
```

## Build Tips

When gets an unexpected top-level property error, babelOptions goes in parserOptions, not at the root:

```json
{
    "parser": "@babel/eslint-parser",
    "parserOptions": {
        "requireConfigFile" : "false"
    },
    "babelOptions": {
        "configFile": "./.babelrc",
    }
}
```
