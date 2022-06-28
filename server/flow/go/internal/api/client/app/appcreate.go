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

package app

import (
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/api/model"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// these consts are used to ensure users can't spam huge entries into our database
const (
	formFieldLen    = 64
	formRedirectLen = 512
)

// AppsPOSTHandler swagger:operation POST /api/v1/apps appCreate
//
// Register a new application on this instance.
//
// The registered application can be used to obtain an application token.
// This can then be used to register a new account, or (through user auth) obtain an access token.
//
// The parameters can also be given in the body of the request, as JSON, if the content-type is set to 'application/json'.
// The parameters can also be given in the body of the request, as XML, if the content-type is set to 'application/xml'.
//
// ---
// tags:
// - apps
//
// consumes:
// - application/json
// - application/xml
// - application/x-www-form-urlencoded
//
// produces:
// - application/json
//
// responses:
//   '200':
//     description: "The newly-created application."
//     schema:
//       "$ref": "#/definitions/application"
//   '400':
//      description: bad request
//   '401':
//      description: unauthorized
//   '403':
//      description: forbidden
//   '404':
//      description: not found
//   '406':
//      description: not acceptable
//   '500':
//      description: internal server error
func (m *Module) AppsPOSTHandler(c *gin.Context) {
	authed, err := oauth.Authed(c, false, false, false, false)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorUnauthorized(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	form := &model.ApplicationCreateRequest{}
	if err := c.ShouldBind(form); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if len(form.ClientName) > formFieldLen {
		err := fmt.Errorf("client_name must be less than %d bytes", formFieldLen)
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if len(form.RedirectURIs) > formRedirectLen {
		err := fmt.Errorf("redirect_uris must be less than %d bytes", formRedirectLen)
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if len(form.Scopes) > formFieldLen {
		err := fmt.Errorf("scopes must be less than %d bytes", formFieldLen)
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if len(form.Website) > formFieldLen {
		err := fmt.Errorf("website must be less than %d bytes", formFieldLen)
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	apiApp, errWithCode := m.processor.AppCreate(c.Request.Context(), authed, form)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	c.JSON(http.StatusOK, apiApp)
}
