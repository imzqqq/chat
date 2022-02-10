# Instance follows & redundancy

## Instances follows

Following servers and being followed as a server ensure visibility of your videos on other instances, and visibility of their videos on your instance. Both are important concepts of Tube as they allow instances and their users to interact.

?> **What is a "follow":** a follow is [a kind of activity](https://www.w3.org/TR/activitypub/#follow-activity-inbox) in the ActivityPub linguo. It allows to subscribe to a server's user activity in the Tube realm.

### Following an instance

Following an instance will display videos of that instance on your pages (i.e. "Trending", "Recently Added", etc.) and your users will be able to interact with them.

You can discover other public instances on https://instances.joinpeertube.org/instances.

You can add an instance to follow and remove instances you follow in `Administration > Federation > Following > Follow` button, and add hostnames of the instances you want to follow there.

![image Adding servers to follow](/assets/admin-add-follow.png)


### Automatically follow other instances

You can choose to automatically follow other instances in `Administration > Configuration > Basic` by specifiying an URL.
Tube will periodically get the URL and follow instances from it.

The field accepts an URL that answers with a JSON object. So it could be a Tube index instance like https://instances.joinpeertube.org/api/v1/instances/hosts or a JSON file hosted on Github, Gitlab etc. See the [Tube index README](https://framagit.org/framasoft/tube/instances-tube) to know the JSON format.

We **encourage you to create your [own Tube index](https://framagit.org/framasoft/tube/instances-tube)**, that can be theme-based or moderated according to your rules. This way, we could imagine a group of Tube admins following the same index on which they only add instances they trust.


### Being followed by an instance

Being followed will display videos of your instance on your followers' pages, and their users will be able to interact with your videos.

You can choose to disable followers or manually approve them in the `Administration > Configuration > Basic` page, by (un)checking **Other instances can follow yours** and **Manually approve new instance followers** boxes.


## Instances redundancy

Tube has a built-in videos redundancy system, allowing an instance to duplicate and serve videos of another instance.
If your instance duplicates a video, web browser will randomly download segments of this video on the origin instance and on your instance, relieving origin instance bandwidth.

### Enable this feature

To enable redundancy on your server, update your [production.yaml file](https://github.com/Chocobozzz/Tube/blob/develop/config/production.yaml.example) and uncomment the redundancy strategies your want.
You could for example choose to duplicate the most viewed or the recently added videos. If your instance should act as a complete mirror for another instance, use the "recently-added" strategy and set the view count to 0

Then, go on the web interface and choose which other followed instance you want to enable redundancy in `Administration > Federation > Following > Follow`

![image showing following panel](/assets/enable-redundancy.png)

Tube will check regularly new videos to duplicate for every strategy (`check_interval` configuration key).
If the cache of a particular strategy is full, Tube will remove old videos and try to duplicate other ones (`min_lifetime` configuration key).

### Checklist to make redundancy work

If you are using the redundancy system to help other instances, make sure that your reverse proxy allows all origins
in CORS. This is of utmost importance, since otherwise clients will simply not be able to download parts of their videos
from you. Also make sure that your instance's response time and bandwidth are good.
