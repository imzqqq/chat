# JoinTube

## Install dependencies

Install the package manager

```sh
npm install --global yarn
```

Fetch the dependencies

```sh
yarn install --pure-lockfile
```

## Dev

```sh
npm run serve
```

## Build for production

```sh
npm run build
```

## Update translations

Install easygettext

```sh
npm install -g easygettext
```

Add Weblate remote:

```sh
git remote add weblate https://weblate.framasoft.org/git/joinpeertube/main
```

Update from Weblate:

```sh
git fetch weblate && git merge weblate/master
```

Re generate translations:

```sh
npm run i18n:update
```

Push on master (Weblate will be automatically updated)

```sh
git push origin master
```

## News

To add a news, add markdown files in `src/news/en` and `src/news/fr` and rebuild `npm run build`.
To archive a news, move it in `src/news/archives` and rebuild `npm run build`.

## Add locale

Add the locale in `src/main.js` and `Makefile`. Then update translations.
