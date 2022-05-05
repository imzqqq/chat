# Little Boxes

Tiny [ActivityPub](https://activitypub.rocks/) framework written in Python, both database and server agnostic.

**Still in early development, and not published on PyPI yet.**

Until a first version is released, the main goal of this framework is to power the [microblog.pub microblog engine](http://github.com/imzqqq/microblog.pub).

## Features

 - Database and server agnostic
   - You need to implement a backend that respond to activity side-effects
   - This also mean you're responsible for serving the activities/collections and receiving them
 - ActivityStreams helper classes
   - with Outbox/Inbox abstractions
 - Content helper using Markdown
   - with helpers for parsing hashtags and linkify content
 - Key (RSA) helper
 - HTTP signature helper
 - JSON-LD signature helper
 - Webfinger helper


## Getting Started

```python
from little_boxes import activitypub as ap

from mydb import db_client


class MyBackend(ap.Backend):

    def __init__(self, db_connection):
        self.db_connection = db_connection    

    def inbox_new(self, as_actor: ap.Person, activity: ap.Activity) -> None:
        # Save activity as "as_actor"
        # [...]

    def post_to_remote_inbox(self, as_actor: ap.Person, payload: ap.ObjectType, recipient: str) -> None:
        # Send the activity to the remote actor
        # [...]


db_con = db_client()
my_backend = MyBackend(db_con)

ap.use_backend(my_backend)

me = ap.Person({})  # Init an actor
outbox = ap.Outbox(me)

follow = ap.Follow(actor=me.id, object='http://iri-i-want-follow')
outbox.post(follow)
```
