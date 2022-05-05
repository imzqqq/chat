# Install from sources
## Instructions
Follow the [build instructions](../build.md) then:

### Prepare files and directories:
```bash
# Create a dedicated user
useradd -r ma1sd

# Create config directory
mkdir -p /etc/ma1sd

# Create data directory and set ownership
mkdir -p /var/lib/ma1sd
chown -R ma1sd /var/lib/ma1sd

# Create bin directory, copy the jar and launch scriot to bin directory
mkdir /usr/lib/ma1sd
cp ./build/libs/ma1sd.jar /usr/lib/ma1sd/
cp ./src/script/ma1sd /usr/lib/ma1sd
chown -R ma1sd /usr/lib/ma1sd
chmod a+x /usr/lib/ma1sd/ma1sd

# Create symlink for easy exec
ln -s /usr/lib/ma1sd/ma1sd /usr/bin/ma1sd
```

### Prepare config file
Copy the configuration file you've created following the build instructions to `/etc/ma1sd/ma1sd.yaml`

### Prepare Systemd
1. Copy `src/systemd/ma1sd.service` to `/etc/systemd/system/` and edit if needed
2. Enable service for auto-startup
```bash
systemctl enable ma1sd
```

### Run
```bash
systemctl start ma1sd
```

## Debug
ma1sd logs to stdout, which is normally sent to `/var/log/syslog` or `/var/log/messages`.
