# Flow Docs

## Quick Start

### Mac

```bash
brew install hugo
hugo serve
```

### Ubuntu

Install hugo

```bash
wget https://github.com/gohugoio/hugo/releases/download/v0.88.1/hugo_extended_0.88.1_Linux-64bit.deb
sudo dpkg -i hugo_extended_0.88.1_Linux-64bit.deb

# Build and Install the Binaries from Source Instead
mkdir $HOME/src
cd $HOME/src
git clone https://github.com/gohugoio/hugo.git
cd hugo
CGO_ENABLED=1 go install --tags extended
```

Run dev

```bash
hugo serve
```

Run prod

```bash
hugo -D
```

Open <http://localhost:1313> in browser
