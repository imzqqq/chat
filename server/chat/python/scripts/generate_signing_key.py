#!/usr/bin/env python

import argparse
import sys

from signedjson.key import generate_signing_key, write_signing_keys

from chat.util.stringutils import random_string

if __name__ == "__main__":
    parser = argparse.ArgumentParser()

    parser.add_argument(
        "-o",
        "--output_file",
        type=argparse.FileType("w"),
        default=sys.stdout,
        help="Where to write the output to",
    )
    args = parser.parse_args()

    key_id = "a_" + random_string(4)
    key = (generate_signing_key(key_id),)
    write_signing_keys(args.output_file, key)
