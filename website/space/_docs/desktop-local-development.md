---
title: Developing Flow locally
layout: doc
product: Flow Desktop
description: Run Flow locally to build and test changes live.
---

Flow is a React-based product built with Create React App and allows for hot-reloading. Developing new features on Flow is relatively easy to perform.

## Requirements
- Git
- Node.js 10 or higher

## Setting up the environment

First, you'll need to download the Flow source code, either by downloading the source code package from our site or cloning the Git repository:

```bash
git clone https://github.com/hyperspacedev/hyperspace.git
```

Once the repository has been cloned, you'll want to install any Node dependencies and make sure Flow will compile properly:

```bash
npm install
npm start # or npm test
```

## Starting a local server

To start a local server instance of Flow, run the following command in the project directory:

```
npm start
```

This will start the server and let you make changes and see them live.

> Note: In Flow v1.1.0-beta3 and older, you'll need to modify the `location` field in your config.json to use `https://localhost:3000` as the address.