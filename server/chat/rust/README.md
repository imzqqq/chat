# A Chat homeserver written in Rust

## What is the goal?

An efficient Chat homeserver that's easy to set up and just works. You can install
it on a mini-computer like the Raspberry Pi to host Chat for your family,
friends or company.

## What is the current status?

As of 2021-09-01, Conduit is Beta, meaning you can join and participate in most
Chat rooms, but not all features are supported and you might run into bugs
from time to time.

There are still a few important features missing:

- E2EE verification over federation
- Outgoing read receipts, typing, presence over federation

Check out the [Conduit 1.0 Release Milestone](https://gitlab.com/famedly/conduit/-/milestones/3).

## How can I deploy my own?

Simple install (this was tested the most): [DEPLOY.md](DEPLOY.md)\
Debian package: [debian/README.Debian](debian/README.Debian)\
Docker: [docker/README.md](docker/README.md)

If you want to connect an Appservice to Conduit, take a look at [APPSERVICES.md](APPSERVICES.md).

## Tech stack

Libraries we use, for example:

- Ruma: A clean library for the Chat Spec in Rust
- Rocket: A flexible web framework
