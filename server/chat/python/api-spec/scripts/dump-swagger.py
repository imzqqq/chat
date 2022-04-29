#!/usr/bin/env python3

# dump-swagger reads all of the swagger API docs used in spec generation and
# outputs a JSON file which merges them all, for use as input to a swagger UI
# viewer.
# See https://github.com/swagger-api/swagger-ui for details of swagger-ui.

import argparse
import errno
import json
import logging
import os.path
import re
import sys
import yaml


scripts_dir = os.path.dirname(os.path.abspath(__file__))
api_dir = os.path.join(os.path.dirname(scripts_dir), "data", "api")

def resolve_references(path, schema):
    if isinstance(schema, dict):
        # do $ref first
        if '$ref' in schema:
            value = schema['$ref']
            path = os.path.join(os.path.dirname(path), value)
            with open(path, encoding="utf-8") as f:
                ref = yaml.safe_load(f)
            result = resolve_references(path, ref)
            del schema['$ref']
        else:
            result = {}

        for key, value in schema.items():
            result[key] = resolve_references(path, value)
        return result
    elif isinstance(schema, list):
        return [resolve_references(path, value) for value in schema]
    else:
        return schema


parser = argparse.ArgumentParser(
    "dump-swagger.py - assemble the Swagger specs into a single JSON file"
)
parser.add_argument(
    "--client_release", "-c", metavar="LABEL",
    default="unstable",
    help="""The client-server release version to generate for. Default:
    %(default)s""",
)
parser.add_argument(
    "-o", "--output",
    default=os.path.join(scripts_dir, "swagger", "api-docs.json"),
    help="File to write the output to. Default: %(default)s"
)
args = parser.parse_args()

output_file = os.path.abspath(args.output)
release_label = args.client_release

major_version = release_label
match = re.match("^(r\d+)(\.\d+)*$", major_version)
if match:
    major_version = match.group(1)

logging.basicConfig()

output = {
    "basePath": "/",
    "consumes": ["application/json"],
    "produces": ["application/json"],
    "host": "chat.imzqqq.top",
    # The servers value will be picked up by RapiDoc to provide a way
    # to switch API servers. Useful when one wants to test compliance
    # of their server with the API.
    "servers": [{
        "url": "https://{homeserver_address}/",
        "variables": {
            "homeserver_address": {
                "default": "matrix-client.chat.imzqqq.top",
                "description": "The base URL for your homeserver",
            }
        }
    }],
    "schemes": ["https"],
    "info": {
        "title": "Chat Client-Server API",
        "version": release_label,
    },
    "securityDefinitions": {},
    "paths": {},
    "swagger": "2.0",
}

cs_api_dir = os.path.join(api_dir, 'client-server')
with open(os.path.join(cs_api_dir, 'definitions',
                       'security.yaml')) as f:
    output['securityDefinitions'] = yaml.safe_load(f)

for filename in os.listdir(cs_api_dir):
    if not filename.endswith(".yaml"):
        continue
    filepath = os.path.join(cs_api_dir, filename)

    print("Reading swagger API: %s" % filepath)
    with open(filepath, "r") as f:
        api = yaml.safe_load(f.read())
        api = resolve_references(filepath, api)

        basePath = api['basePath']
        for path, methods in api["paths"].items():
            for method, spec in methods.items():
                if "tags" in spec.keys():
                    if path not in output["paths"]:
                        output["paths"][path] = {}
                    output["paths"][path][method] = spec

print("Generating %s" % output_file)

try:
    os.makedirs(os.path.dirname(output_file))
except OSError as e:
    if e.errno != errno.EEXIST:
        raise

with open(output_file, "w") as f:
    text = json.dumps(output, sort_keys=True, indent=4)
    text = text.replace("%CLIENT_RELEASE_LABEL%", release_label)
    f.write(text)
