#!/usr/bin/env python

# Runs an HTTP server on localhost:8000 which will serve the generated swagger
# JSON so that it can be viewed in an online swagger UI.

import argparse
import os
import http.server
import socketserver

# Thanks to http://stackoverflow.com/a/13354482
class MyHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    def end_headers(self):
        self.send_my_headers()
        http.server.SimpleHTTPRequestHandler.end_headers(self)

    def send_my_headers(self):
        self.send_header("Access-Control-Allow-Origin", "*")


if __name__ == '__main__':
    scripts_dir = os.path.dirname(os.path.abspath(__file__))
    parser = argparse.ArgumentParser()
    parser.add_argument(
        '--port', '-p',
        type=int, default=8000,
        help='TCP port to listen on (default: %(default)s)',
    )
    parser.add_argument(
        'swagger_dir', nargs='?',
        default=os.path.join(scripts_dir, 'swagger'),
        help='directory to serve (default: %(default)s)',
    )
    args = parser.parse_args()

    os.chdir(args.swagger_dir)

    httpd = socketserver.TCPServer(("localhost", args.port),
                                   MyHTTPRequestHandler)
    print("Serving at http://localhost:%i/api-docs.json" % args.port)
    httpd.serve_forever()
