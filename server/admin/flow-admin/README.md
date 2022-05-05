# Flow Admin Panel

Standalone web admin panel for [GoToSocial](https://github.com/superseriousbusiness/gotosocial).

A public hosted instance is also available at <https://testingtesting123.xyz/admin>, so you can fill your own instance URL in there.

## Installation

Build requirements: some version of Node.js with npm,

```bash
npm install
node index.js
```

All processed build output will now be in `public/`, which you can copy over to a folder in your GoToSocial installation like `web/assets/admin`, or serve elsewhere.
No further configuration is required, authentication happens through normal OAUTH flow.

## Development

Follow the installation steps, but run `NODE_ENV=development node index.js` to start the livereloading dev server instead.
