FROM gitpod/workspace-postgres

# Gitpod will not rebuild Tube's dev image unless *some* change is made to this Dockerfile.
# To trigger a rebuild, simply increase this counter:
ENV TRIGGER_REBUILD 2

# Install Tube's dependencies.
RUN sudo apt-get update -q && sudo apt-get install -qy \
 ffmpeg \
 openssl \
 redis-server
