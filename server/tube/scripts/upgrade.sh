#!/bin/sh

set -eu

PEERTUBE_PATH=${1:-/var/www/tube}

if [ ! -e "$PEERTUBE_PATH" ]; then
  echo "Error - path \"$PEERTUBE_PATH\" wasn't found"
  echo ""
  echo "If tube was installed in another path, you can specify it with"
  echo "    ./upgrade.sh <PATH>"
  exit 1
fi

if [ ! -e "$PEERTUBE_PATH/versions" -o ! -e "$PEERTUBE_PATH/config/production.yaml" ]; then
  echo "Error - Couldn't find tube installation in \"$PEERTUBE_PATH\""
  echo ""
  echo "If tube was installed in another path, you can specify it with"
  echo "    ./upgrade.sh <PATH>"
  exit 1
fi

if [ -x "$(command -v awk)" ] && [ -x "$(command -v sed)" ] ; then
    REMAINING=$(df -k $PEERTUBE_PATH | awk '{ print $4}' | sed -n 2p)
    ONE_GB=$((1024 * 1024))
    if [ "$REMAINING" -lt "$ONE_GB" ]; then
    echo "Error - not enough free space for upgrading"
    echo ""
    echo "Make sure you have at least 1 GB of free space in $PEERTUBE_PATH"
    exit 1
    fi
fi

# Backup database
if [ -x "$(command -v pg_dump)" ]
then
  SQL_BACKUP_PATH="$PEERTUBE_PATH/backup/sql-peertube_prod-$(date +"%Y%m%d-%H%M").bak"
  DB_USER=$(node -e "console.log(require('js-yaml').load(fs.readFileSync('$PEERTUBE_PATH/config/production.yaml', 'utf8'))['database']['username'])")
  DB_PASS=$(node -e "console.log(require('js-yaml').load(fs.readFileSync('$PEERTUBE_PATH/config/production.yaml', 'utf8'))['database']['password'])")
  DB_HOST=$(node -e "console.log(require('js-yaml').load(fs.readFileSync('$PEERTUBE_PATH/config/production.yaml', 'utf8'))['database']['hostname'])")
  DB_SUFFIX=$(node -e "console.log(require('js-yaml').load(fs.readFileSync('$PEERTUBE_PATH/config/production.yaml', 'utf8'))['database']['suffix'])")
  DB_NAME=$(node -e "console.log(require('js-yaml').load(fs.readFileSync('$PEERTUBE_PATH/config/production.yaml', 'utf8'))['database']['name'] || '')")
  mkdir -p $PEERTUBE_PATH/backup
  PGPASSWORD=$DB_PASS pg_dump -U $DB_USER -h $DB_HOST -F c "${DB_NAME:-tube${DB_SUFFIX}}" -f "$SQL_BACKUP_PATH"
else
  echo "pg_dump not found. Cannot make a SQL backup!"
fi

# If there is a pre-release, give the user a choice which one to install.
RELEASE_VERSION=$(curl -s https://api.github.com/repos/chocobozzz/tube/releases/latest | grep tag_name | cut -d '"' -f 4)
PRE_RELEASE_VERSION=$(curl -s https://api.github.com/repos/chocobozzz/tube/releases | grep tag_name | head -1 | cut -d '"' -f 4)

if [ "$RELEASE_VERSION" != "$PRE_RELEASE_VERSION" ]; then
  echo -e "Which version do you want to install?\n[1] $RELEASE_VERSION (stable) \n[2] $PRE_RELEASE_VERSION (pre-release)"
  read choice
  case $choice in
      [1]* ) VERSION="$RELEASE_VERSION";;
      [2]* ) VERSION="$PRE_RELEASE_VERSION";;
      * ) exit;
  esac
else
  VERSION="$RELEASE_VERSION"
fi

echo "Installing Peertube version $VERSION"
wget -q "https://github.com/Chocobozzz/Tube/releases/download/${VERSION}/tube-${VERSION}.zip" -O "$PEERTUBE_PATH/versions/tube-${VERSION}.zip"
cd $PEERTUBE_PATH/versions
unzip -o "tube-${VERSION}.zip"
rm -f "tube-${VERSION}.zip"

# Launch yarn to check if we have all required dependencies
cd "$PEERTUBE_PATH/versions/tube-${VERSION}"
NOCLIENT=1 yarn install --production --pure-lockfile

# Switch to latest code version
rm -rf $PEERTUBE_PATH/tube-latest
ln -s "$PEERTUBE_PATH/versions/tube-${VERSION}" $PEERTUBE_PATH/tube-latest
cp $PEERTUBE_PATH/tube-latest/config/default.yaml $PEERTUBE_PATH/config/default.yaml

echo "Differences in configuration files..."
diff -u $PEERTUBE_PATH/config/production.yaml "$PEERTUBE_PATH/versions/tube-${VERSION}/config/production.yaml.example"

echo ""
echo "==========================================="
echo "==   Don’t forget to restart Tube!   =="
echo "==========================================="
