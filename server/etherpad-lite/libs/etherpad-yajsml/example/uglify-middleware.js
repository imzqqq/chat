/*
 * Copyright (c) 2011 Chad Weider
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

'use strict';

const Uglify = require('uglify-js');

module.exports = class UglifyMiddleware {
  handle(req, res, next) {
    const oldRes = {};
    oldRes.writeHead = res.writeHead;

    const console = this._console;

    res.writeHead = (status, headers) => {
      if (headers &&
          headers['content-type'] &&
          headers['content-type'].indexOf('application/javascript;') === 0 &&
          req.cookies && req.cookies['js-compress-override'] !== 'bypass'
      ) {
        let buffer = '';
        oldRes.write = res.write;
        oldRes.end = res.end;
        res.write = (data, encoding) => {
          buffer += data.toString(encoding);
        };
        res.end = (data, encoding) => {
          if (data) {
            res.write(data, encoding);
          }

          let content = undefined;
          try {
            let ast = Uglify.parser.parse(buffer);
            ast = Uglify.uglify.ast_mangle(ast);
            ast = Uglify.uglify.ast_squeeze(ast);
            content = Uglify.uglify.gen_code(ast);
          } catch (e) {
            // Silence error?
            console && console.error(e);
            content = buffer;
          }

          res.write = oldRes.write;
          res.end = oldRes.end;
          content && res.write(content, 'utf8');
          res.end();
        };
      }

      res.writeHead = oldRes.writeHead;
      res.writeHead(status, headers);
    };

    next(undefined, req, res);
  }
};
