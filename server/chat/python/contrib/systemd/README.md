# Setup Chat with Systemd
This is a setup for managing chat with a user contributed systemd unit 
file. It provides a `chat-server` systemd unit file that should be tailored 
to accommodate your installation in accordance with the installation 
instructions provided in
[installation instructions](https://chat.docs.imzqqq.top/setup/installation.html).

## Setup
1. Under the service section, ensure the `User` variable matches which user
you installed chat under and wish to run it as. 
2. Under the service section, ensure the `WorkingDirectory` variable matches
where you have installed chat.
3. Under the service section, ensure the `ExecStart` variable matches the
appropriate locations of your installation.
4. Copy the `chat-server.service` to `/etc/systemd/system/`
5. Start Chat: `sudo systemctl start chat-server`
6. Verify Chat is running: `sudo systemctl status chat-server`
7. *optional* Enable Chat to start at system boot: `sudo systemctl enable chat-server`
