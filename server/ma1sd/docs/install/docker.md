---

** Outdated due to migrating to fork. **

---

# Docker
## Fetch
Pull the latest stable image:
```bash
docker pull ma1uta/ma1sd
```

## Configure
On first run, simply using `MATRIX_DOMAIN` as an environment variable will create a default config for you.  
You can also provide a configuration file named `ma1sd.yaml` in the volume mapped to `/etc/ma1sd` before starting your
container.

## Run
Use the following command after adapting to your needs:
- The `MATRIX_DOMAIN` environment variable to yours
- The volumes host paths

```bash
docker run --rm -e MATRIX_DOMAIN=example.org -v /data/ma1sd/etc:/etc/ma1sd -v /data/ma1sd/var:/var/ma1sd -p 8090:8090 -t ma1uta/ma1sd
```

For more info, including the list of possible tags, see [the public repository](https://hub.docker.com/r/ma1uta/ma1sd/)

## Troubleshoot
Please read the [Troubleshooting guide](../troubleshooting.md).
