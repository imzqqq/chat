# Application behind search.joinpeertube.org

## Dev

```terminal
$ git submodule update --init --recursive
$ yarn install --pure-lockfile
```

The database (Elastic Search) is automatically created by Tube at startup.

Run simultaneously (for example with 3 terminals):

```terminal
$ tsc -w
```

```terminal
$ node dist/server
```

```
$ cd client && npm run serve
```

Then open http://localhost:8080.

### Add locale

Add the locale in `client/src/main.ts` and `client/Makefile`. Then update translations.

## Production

Install dependencies:
  * NodeJS (v12)
  * Elastic Search

```terminal
$ git clone https://framagit.org/framasoft/tube/search-index.git /var/www/tube-search-index
$ cd /var/www/tube-search-index
$ git submodule update --init --recursive
$ yarn install --pure-lockfile
$ npm run build
$ cp config/default.yaml config/production.yaml
$ vim config/production.yaml
$ NODE_ENV=production NODE_CONFIG_DIR=/var/www/tube-search-index/config node dist/server.js
```

### Mapping migration

To update Elastic Search index mappings without downtime, run another instance of the search indexer
using the same configuration that the main node. You just have to update `elastic-search.indexes.*` to use new index names.

```
$ cd /var/www/tube-search-index
$ cp config/production.yaml config/production-1.yaml
$ vim config/production-1.yaml
$ NODE_ENV=production NODE_APP_INSTANCE=1 NODE_CONFIG_DIR=/var/www/tube-search-index/config node dist/server.js
```

After a while the new indexes will be filled. You can then stop the second indexer, update `config/production.yaml` to use
the new index names and restart the main index.

