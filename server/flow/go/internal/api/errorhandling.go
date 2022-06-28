/*
   GoToSocial
   Copyright (C) 2021-2022 GoToSocial Authors admin@gotosocial.org

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Affero General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Affero General Public License for more details.

   You should have received a copy of the GNU Affero General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package api

import (
	"context"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
	apimodel "github.com/superseriousbusiness/gotosocial/internal/api/model"
	"github.com/superseriousbusiness/gotosocial/internal/config"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
)

// TODO: add more templated html pages here for different error types

// NotFoundHandler serves a 404 html page through the provided gin context,
// if accept is 'text/html', or just returns a json error if 'accept' is empty
// or application/json.
//
// When serving html, NotFoundHandler calls the provided InstanceGet function
// to fetch the apimodel representation of the instance, for serving in the
// 404 header and footer.
//
// If an error is returned by InstanceGet, the function will panic.
func NotFoundHandler(c *gin.Context, instanceGet func(ctx context.Context, domain string) (*apimodel.Instance, gtserror.WithCode), accept string) {
	switch accept {
	case string(TextHTML):
		host := config.GetHost()
		instance, err := instanceGet(c.Request.Context(), host)
		if err != nil {
			panic(err)
		}

		c.HTML(http.StatusNotFound, "404.tmpl", gin.H{
			"instance": instance,
		})
	default:
		c.JSON(http.StatusNotFound, gin.H{"error": http.StatusText(http.StatusNotFound)})
	}
}

// genericErrorHandler is a more general version of the NotFoundHandler, which can
// be used for serving either generic error pages with some rendered help text,
// or just some error json if the caller prefers (or has no preference).
func genericErrorHandler(c *gin.Context, instanceGet func(ctx context.Context, domain string) (*apimodel.Instance, gtserror.WithCode), accept string, errWithCode gtserror.WithCode) {
	switch accept {
	case string(TextHTML):
		host := config.GetHost()
		instance, err := instanceGet(c.Request.Context(), host)
		if err != nil {
			panic(err)
		}

		c.HTML(errWithCode.Code(), "error.tmpl", gin.H{
			"instance": instance,
			"code":     errWithCode.Code(),
			"error":    errWithCode.Safe(),
		})
	default:
		c.JSON(errWithCode.Code(), gin.H{"error": errWithCode.Safe()})
	}
}

// ErrorHandler takes the provided gin context and errWithCode and tries to serve
// a helpful error to the caller. It will do content negotiation to figure out if
// the caller prefers to see an html page with the error rendered there. If not, or
// if something goes wrong during the function, it will recover and just try to serve
// an appropriate application/json content-type error.
func ErrorHandler(c *gin.Context, errWithCode gtserror.WithCode, instanceGet func(ctx context.Context, domain string) (*apimodel.Instance, gtserror.WithCode)) {
	path := c.Request.URL.Path
	if raw := c.Request.URL.RawQuery; raw != "" {
		path = path + "?" + raw
	}

	l := logrus.WithFields(logrus.Fields{
		"path":  path,
		"error": errWithCode.Error(),
	})

	statusCode := errWithCode.Code()

	if statusCode == http.StatusInternalServerError {
		l.Error("Internal Server Error")
	} else {
		l.Debug("handling error")
	}

	// if we panic for any reason during error handling,
	// we should still try to return a basic code
	defer func() {
		if p := recover(); p != nil {
			l.Warnf("recovered from panic: %s", p)
			c.JSON(statusCode, gin.H{"error": errWithCode.Safe()})
		}
	}()

	// discover if we're allowed to serve a nice html error page,
	// or if we should just use a json. Normally we would want to
	// check for a returned error, but if an error occurs here we
	// can just fall back to default behavior (serve json error).
	accept, _ := NegotiateAccept(c, HTMLOrJSONAcceptHeaders...)

	if statusCode == http.StatusNotFound {
		// use our special not found handler with useful status text
		NotFoundHandler(c, instanceGet, accept)
	} else {
		genericErrorHandler(c, instanceGet, accept, errWithCode)
	}
}

// OAuthErrorHandler is a lot like ErrorHandler, but it specifically returns errors
// that are compatible with https://datatracker.ietf.org/doc/html/rfc6749#section-5.2,
// but serializing errWithCode.Error() in the 'error' field, and putting any help text
// from the error in the 'error_description' field. This means you should be careful not
// to pass any detailed errors (that might contain sensitive information) into the
// errWithCode.Error() field, since the client will see this. Use your noggin!
func OAuthErrorHandler(c *gin.Context, errWithCode gtserror.WithCode) {
	l := logrus.WithFields(logrus.Fields{
		"path":  c.Request.URL.Path,
		"error": errWithCode.Error(),
		"help":  errWithCode.Safe(),
	})

	statusCode := errWithCode.Code()

	if statusCode == http.StatusInternalServerError {
		l.Error("Internal Server Error")
	} else {
		l.Debug("handling OAuth error")
	}

	c.JSON(statusCode, gin.H{
		"error":             errWithCode.Error(),
		"error_description": errWithCode.Safe(),
	})
}
