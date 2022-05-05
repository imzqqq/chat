# From source
- [Binaries](#binaries)
  - [Requirements](#requirements)
  - [Build](#build)
- [Debian package](#debian-package)
- [Docker image](#docker-image)
- [Next steps](#next-steps)

## Binaries
### Requirements
- JDK 1.8
- OpenJDK 11
- OpenJDK 14

### Build
```bash
git clone https://github.com/ma1uta/ma1sd.git
cd ma1sd
./gradlew build
```

Create a new configuration file by coping `ma1sd.example.yaml` to `ma1sd.yaml` and edit to your needs.  
For advanced configuration, see the [Configure section](configure.md).

Start the server in foreground to validate the build and configuration:
```bash
java -jar build/libs/ma1sd.jar
```

Ensure the signing key is available:
```bash
$ curl 'http://localhost:8090/_matrix/identity/api/v1/pubkey/ed25519:0'

{"public_key":"..."}
```

Test basic recursive lookup (requires Internet connection with access to TCP 443):
```bash
$ curl 'http://localhost:8090/_matrix/identity/api/v1/lookup?medium=email&address=ma1sd-federation-test@kamax.io'

{"address":"ma1sd-federation-test@kamax.io","medium":"email","mxid":"@ma1sd-lookup-test:kamax.io",...}
```

If you enabled LDAP, you can also validate your config with a similar request after replacing the `address` value with
something present within your LDAP
```bash
curl 'http://localhost:8090/_matrix/identity/api/v1/lookup?medium=email&address=john.doe@example.org'
```

If you plan on testing the integration with a homeserver, you will need to run an HTTPS reverse proxy in front of it
as the reference Home Server implementation [synapse](https://github.com/matrix-org/synapse) requires a HTTPS connection
to an ID server.  

Next step: [Install your compiled binaries](install/source.md)

## Debian package
Requirements:
- fakeroot
- dpkg-deb

[Build ma1sd](#build) then:
```bash
./gradlew debBuild
```
You will find the debian package in `build/dist`.  
Then follow the instruction in the [Debian package](install/debian.md) document.

## Docker image
[Build ma1sd](#build) then:
```bash
./gradlew dockerBuild
```
Then follow the instructions in the [Docker install](install/docker.md#configure) document.

### Multi-platform builds

Provided with experimental docker feature [buildx](https://docs.docker.com/buildx/working-with-buildx/)
To build the arm64 and amd64 images run:
```bash
./gradlew dockerBuildX
```

## Next steps
- [Integrate with your infrastructure](getting-started.md#integrate)
