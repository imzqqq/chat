# Dockerfile to build the matrixdotorg/chat docker images.
#
# To build the image, run `docker build` command from the root of the
# chat repository:
#
#    docker build -f docker/Dockerfile .
#
# There is an optional PYTHON_VERSION build argument which sets the
# version of python to build against: for example:
#
#    docker build -f docker/Dockerfile --build-arg PYTHON_VERSION=3.6 .
#

ARG PYTHON_VERSION=3.8

###
### Stage 0: builder
###
FROM docker.io/python:${PYTHON_VERSION}-slim as builder

# install the OS build deps
RUN apt-get update && apt-get install -y \
    build-essential \
    libffi-dev \
    libjpeg-dev \
    libpq-dev \
    libssl-dev \
    libwebp-dev \
    libxml++2.6-dev \
    libxslt1-dev \
    openssl \
    rustc \
    zlib1g-dev \
    && rm -rf /var/lib/apt/lists/*

# Copy just what we need to pip install
COPY scripts /chat/scripts/
COPY MANIFEST.in README.rst setup.py synctl /chat/
COPY chat/__init__.py /chat/chat/__init__.py
COPY chat/python_dependencies.py /chat/chat/python_dependencies.py

# To speed up rebuilds, install all of the dependencies before we copy over
# the whole chat project so that we this layer in the Docker cache can be
# used while you develop on the source
#
# This is aiming at installing the `install_requires` and `extras_require` from `setup.py`
RUN pip install --prefix="/install" --no-warn-script-location \
    /chat[all]

# Copy over the rest of the project
COPY chat /chat/chat/

# Install the chat package itself and all of its children packages.
#
# This is aiming at installing only the `packages=find_packages(...)` from `setup.py
RUN pip install --prefix="/install" --no-deps --no-warn-script-location /chat

###
### Stage 1: runtime
###

FROM docker.io/python:${PYTHON_VERSION}-slim

LABEL org.opencontainers.image.url='https://matrix.org/docs/projects/server/chat'
LABEL org.opencontainers.image.documentation='https://github.com/matrix-org/chat/blob/master/docker/README.md'
LABEL org.opencontainers.image.source='https://github.com/matrix-org/chat.git'
LABEL org.opencontainers.image.licenses='Apache-2.0'

RUN apt-get update && apt-get install -y \
    curl \
    gosu \
    libjpeg62-turbo \
    libpq5 \
    libwebp6 \
    xmlsec1 \
    libjemalloc2 \
    libssl-dev \
    openssl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /install /usr/local
COPY ./docker/start.py /start.py
COPY ./docker/conf /conf

VOLUME ["/data"]

EXPOSE 8080/tcp 8009/tcp 8880/tcp

ENTRYPOINT ["/start.py"]

HEALTHCHECK --start-period=5s --interval=15s --timeout=5s \
    CMD curl -fSs http://localhost:8080/health || exit 1
