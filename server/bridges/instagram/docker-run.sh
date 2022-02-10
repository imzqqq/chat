#!/bin/sh
cd /opt/mautrix-instagram

if [ ! -f /data/config.yaml ]; then
	cp example-config.yaml /data/config.yaml
	echo "Didn't find a config file."
	echo "Copied default config file to /data/config.yaml"
	echo "Modify that config file to your liking."
	echo "Start the container again after that to generate the registration file."
	exit
fi

if [ ! -f /data/registration.yaml ]; then
	python3 -m mautrix_instagram -g -c /data/config.yaml -r /data/registration.yaml
	exit
fi

exec python3 -m mautrix_instagram -c /data/config.yaml
