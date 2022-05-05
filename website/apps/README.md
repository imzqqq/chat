# Chat Website2

This repo is for the static Chat website.

## To test locally

Run `npm i express` then `node runner.js`

## Production

- `npm install -g pm2`
- `pm2 list`
- `pm2 start ./runner.js`
- `pm2 stop app_name|app_id`
- `pm2 stop all`
- Start on boot: `pm2 startup`
