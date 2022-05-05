# Remote storage (S3)

In Tube version 3.4.0 native support was added for storage in S3 compatible
object stores. If you are using this version or newer you can use the new method
for s3 storage, otherwise you can still follow the documentation for the
[old method](admin-remote-storage?id=old-object-storage-method).

## Native object storage

**Tube >= 3.4**

If your object storage provider supports the AWS S3 API, you can configure your
instance to move files there after transcoding. The bucket you configure should
be public and have CORS rules to allow traffic from anywhere.

Live videos are still stored on the disk. If replay is enabled, they will be moved
in the object storage after transcoding.

### Tube Settings

#### Endpoint and buckets

Here are two examples on how you can configure your instance:

```yaml
# Store all videos in one bucket on Backblaze b2
object_storage:
  enabled: true

  # Example Backblaze b2 endpoint
  endpoint: 's3.us-west-001.backblazeb2.com'

  videos:
    bucket_name: 'tube-videos'
    prefix: 'videos/'

  # Use the same bucket as for webtorrent videos but with a different prefix
  streaming_playlists:
    bucket_name: 'tube-videos'
    prefix: 'streaming-playlists/'
```

```yaml
# Use two different buckets for webtorrent and HLS videos on AWS S3
object_storage:
  enabled: true

  # Example AWS endpoint in the us-east-1 region
  endpoint: 's3.us-east-1.amazonaws.com'
  # Needs to be set to the bucket region when using AWS S3
  region: 'us-east-1'

  videos:
    bucket_name: 'webtorrent-videos'
    prefix: ''

  streaming_playlists:
    bucket_name: 'hls-videos'
    prefix: ''
```


#### Credentials

You will also need to supply credentials to the S3 client. The official AWS
S3 library is used in Tube, which supports multiple [credential loading methods](https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/setting-credentials-node.html).

If you set the credentials in the configuration file, this will override
credentials from the environment or a `~/.aws/credentials` file. When loading
from the environment, the usual `AWS_ACCESS_KEY_ID` and `AWS_ACCESS_KEY_ID`
variables are used.

#### Cache server

To reduce object storage cost, we strongly recommend to setup a cache server (CDN/external proxy).
Set your mirror/CDN URL in `object_storage.{streaming_playlists,videos}.base_url` and Tube will replace
the object storage host by this base URL on the fly (so you can easily change the `base_url` configuration).

#### Max upload part

If you have trouble with uploads to object storing failing, you can try lowering
the part size. `object_storage.max_upload_part` is set to `2GB` by default, you can
try experimenting with this value to optimize uploading. Multiple uploads can happen
in parallel, but for one video the parts are uploaded sequentially.

### CORS settings

Because the browser will load the objects from object storage from a different URL than
the local Tube instance, cross-origin resource sharing rules apply.

You can solve this either by loading the objects through some kind of caching CDN
that you give access and setting `object_storage.{streaming_playlists,videos}.base_url`
to that caching server, or by allowing access from all origins.

Allowing access from all origins on AWS S3 can be done in the permissions
tab of your bucket settings. You can set the policy to this for example:

```json
[
    {
        "AllowedHeaders": [
            "*"
        ],
        "AllowedMethods": [
            "GET"
        ],
        "AllowedOrigins": [
            "*"
        ],
        "ExposeHeaders": []
    }
]
```

Backblaze B2 has a similar setting, you can set it via the b2 command
line tool:

```bash
b2 update-bucket --corsRules '[
        {
            "allowedHeaders": [
                "range"
            ],
            "allowedOperations": [
                "b2_download_file_by_id",
                "b2_download_file_by_name"
            ],
            "allowedOrigins": [
                "*"
            ],
            "corsRuleName": "downloadFromAnyOrigin",
            "exposeHeaders": null,
            "maxAgeSeconds": 3600
        },
        {
            "allowedHeaders": [
                "range"
            ],
            "allowedOperations": [
                "s3_head",
                "s3_get"
            ],
            "allowedOrigins": [
                "*"
            ],
            "corsRuleName": "s3DownloadFromAnyOrigin",
            "exposeHeaders": null,
            "maxAgeSeconds": 3600
        }
    ]' bucketname allPublic
```

### Migrate videos from filesystem to object storage

**Tube >= 4.0**

Use [create-move-video-storage-job](https://tube.docs.imzqqq.top/maintain-tools?id=create-move-video-storage-jobjs) script.


### Migrate to another object storage provider

Tube stores object URLs in the database, so even if you change the object storage configuration
it will serve previously uploaded videos using the old object storage endpoint while serving new uploads using
the new object storage endpoint.

File URLs migration in Tube is not provided yet.


## Old object storage method

Object storage integration is done via FUSE, for instance with [s3fs](https://github.com/s3fs-fuse/s3fs-fuse).

```bash
export S3_STORAGE=/var/www/tube/s3-storage
mkdir $S3_STORAGE
s3fs your-space-name /var/www/tube/s3-storage -o url=https://region.digitaloceanspaces.com -o allow_other -o use_path_request_style -o uid=1000 -o gid=1000
mount --bind $S3_STORAGE/videos /var/www/tube/storage/videos
mount --bind $S3_STORAGE/redundancy /var/www/tube/storage/redundancy
mount --bind $S3_STORAGE/streaming-playlists /var/www/tube/storage/streaming-playlists
```

?> **Note**: see https://github.com/Chocobozzz/Tube/issues/147 for a list of known
untested equivalents to s3fs.

Now set the base public url of your bucket in your reverse proxy, as shown in the
[official Nginx template](https://github.com/Chocobozzz/Tube/blob/5f59cf077fd9f9c0c91c7bb56efbfd5db103bff2/support/nginx/tube#L235-L242).
Videos should now stream directly from the configured bucket, saving you some significant bandwidth!
