# Continuous integration

Tube uses Github Actions as a CI platform.
CI tasks are described in `.github/workflows`.

## benchmark.yml

*Scheduled*

Run various benchmarks (build, API etc) and upload results on https://builds.joinpeertube.org/tube-stats/ to be publicly consumed.

## codeql.yml

*Scheduled, on push on develop and on pull request*

Run CodeQL task to throw code security issues in Github. https://lgtm.com/projects/g/Chocobozzz/Tube can also be used.

## docker.yml

*Scheduled and on push on master*

Build `chocobozzz/tube-webserver:latest`, `chocobozzz/tube:production-...`, `chocobozzz/tube:v-...` (only latest Tube tag) and `chocobozzz/tube:develop-...` Docker images. Scheduled to automatically upgrade image software (Debian security issues etc).

## nightly.yml

*Scheduled*

Build Tube nightly build (`develop` branch) and upload the release on https://builds.joinpeertube.org/nightly.

## stats.yml

*On push on develop*

Create various Tube stats (line of codes, build size, lighthouse report) and upload results on https://builds.joinpeertube.org/tube-stats/ to be publicly consumed.

## test.yml

*Scheduled, on push and pull request*

Run Tube lint and tests.
