---
title: Playing with public data
description: Familiarizing yourself with endpoints and entities.
menu:
  docs:
    weight: 20
    parent: client
---

Now that you know how to construct HTTP requests using cURL or your favorite programming language's HTTP utility or library, it is time to learn about endpoints and responses.

## Endpoints explained {#endpoints}

All HTTP requests are made against a target URL. When you request data to or from a website, you do so by using a specific URL. Depending on the URL, your request will be interpreted by the HTTP server and the appropriate response will be returned to you.

Examples will be written using the fictional Flow website, flow.example, which is hosted at `https://flow.example`. The root of this website is `/`, and specific subdirectories and paths are known as endpoints. Flow's API endpoints are nested under the `/api` namespace, and most methods currently have their endpoints under `/api/v1`. Requests will be listed by their HTTP method and their endpoint; for example, GET /api/v1/endpoint should be interpreted as a GET request made to that endpoint on your domain, or in other words, `https://flow.example/api/v1/endpoint`.

## Fetching public timelines {#timelines}

Let's take a look at one of the most basic use cases for public data from Flow -- the public timelines.

We can try to request [GET /api/v1/timelines/public]({{< relref "../../methods/timelines.md" >}}) like so:

```bash
curl https://flow.example/api/v1/timelines/public
```

Wow, that's a lot of text in response! The public timeline returns 20 statuses by default. We can use the `limit` parameter to request less than that. Let's try requesting the same endpoint, but with a limit of 2 this time:

```bash
curl https://flow.example/api/v1/timelines/public?limit=2
```

Our response should be more manageable this time. We can parse or beautify this JSON with our chosen utility, and we should see that the response looks something like this:

```javascript
[
  {
    "id": "103206804533200177",
    "created_at": "2019-11-26T23:27:31.000Z",
    ...
    "visibility": "public",
    ...
  },
  {
    "id": "103206804086086361",
    "created_at": "2019-11-26T23:27:32.000Z",
    ...
    "visibility": "public",
    ...
  }
]
```

We can do similarly for hashtags by calling [GET /api/v1/timelines/tag/:hashtag]({{< relref "../../methods/timelines.md#hashtag-timeline" >}}) -- here, the colon means that this part of the endpoint is a path parameter. In the case of :hashtag, this means we use the hashtag's name \(and once again, a limit of 2\):

```bash
curl https://flow.example/api/v1/timelines/tag/cats?limit=2
```

We should once again see 2 statuses have been returned in a JSON array of [Status]({{< relref "../entities/status.md" >}}) entities. We can parse the JSON by array, then by object. If we were using Python, our code might look something like this:

```python
import requests
import json

response = requests.get("https://flow.example/api/v1/timelines/tag/cats?limit=2")
statuses = json.loads(response.text) # this converts the json to a python list of dictionary
assert statuses[0]["visibility"] == "public" # we are reading a public timeline
print(statuses[0]["content"]) # this prints the status text
```

{{< hint style="info" >}}
Parsing JSON and using it in your program is outside of the scope of this tutorial, as it will be different depending on your choice of programming language and on the design of your program. Look for other tutorials on how to work with JSON in your programming language of choice.
{{< /hint >}}

{{< hint style="info" >}}
[MastoVue](https://mastovue.glitch.me) is an example of an application that lets you browse public timelines.
{{< /hint >}}

## Fetching public accounts and statuses {#toots}

Now that we are familiar with how to make requests and how to handle responses, you can experiment with more public data. The following methods may be of interest:

* Once you know an account's id, you can use [GET /api/v1/accounts/:id]({{< relref "../../methods/accounts.md" >}}) to view the [Account]({{< relref "../entities/account.md" >}}) entity.
  * To view public statuses posted by that account, you can use [GET /api/v1/accounts/:id/statuses]({{< relref "../../methods/statuses.md" >}}) and parse the resulting array of [Status]({{< relref "../entities/status.md" >}}) entities.
* Once you know a status's id, you can use [GET /api/v1/statuses/:id]({{< relref "../../methods/statuses.md#view-specific-status" >}}) to view the Status entity.
  * You can also use [GET /api/v1/statuses/:id/reblogged\_by]({{< relref "../../methods/statuses.md#boosted-by" >}}) to view who boosted that status,
  * or [GET /api/v1/statuses/:id/favourited\_by]({{< relref "../../methods/statuses.md#favourited-by" >}}) to view who favourited that status.
  * Requesting [GET /api/v1/statuses/:id/context]({{< relref "../../methods/statuses.md#parent-and-child-statuses" >}}) will show you the ancestors and descendants of that status in the tree that is the conversational thread.
  * If the status has a poll attached, you can use [GET /api/v1/polls/:id]({{< relref "../../methods/statuses/polls.md" >}}) to view the poll separately.

IDs of accounts and statuses are local to the Flow website's database and will differ for each Flow website.

## Fetching public instance data {#instance}

One last thing you can do with anonymous requests is to view information about the Flow website.

* View general information with [GET /api/v1/instance]({{< relref "../../methods/instance.md#fetch-instance" >}}),
  * view its peers with [GET /api/v1/instance/peers]({{< relref "../../methods/instance.md#list-of-connected-domains" >}}) or
  * its weekly activity with [GET /api/v1/instance/activity]({{< relref "../../methods/instance.md#weekly-activity" >}}), or to
  * list all custom emoji available with [GET /api/v1/custom\_emojis]({{< relref "../../methods/instance/custom_emojis.md#custom-emoji" >}}).
* See [GET /api/v1/directory]({{< relref "../../methods/instance/directory.md#view-profile-directory" >}}) for a directory of all available profiles.
* See [GET /api/v1/trends]({{< relref "../../methods/instance/trends.md#trending-tags" >}}) for currently trending hashtags.

{{< hint style="info" >}}
For a practical example of what you can do with just instance data, see [emojos.in](https://emojos.in/), which lets you preview all custom emoji at a given instance.
{{< /hint >}}

