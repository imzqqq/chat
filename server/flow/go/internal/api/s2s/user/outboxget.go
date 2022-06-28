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

package user

import (
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
)

// OutboxGETHandler swagger:operation GET /users/{username}/outbox s2sOutboxGet
//
// Get the public outbox collection for an actor.
//
// Note that the response will be a Collection with a page as `first`, as shown below, if `page` is `false`.
//
// If `page` is `true`, then the response will be a single `CollectionPage` without the wrapping `Collection`.
//
// HTTP signature is required on the request.
//
// ---
// tags:
// - s2s/federation
//
// produces:
// - application/activity+json
//
// parameters:
// - name: username
//   type: string
//   description: Username of the account.
//   in: path
//   required: true
// - name: page
//   type: boolean
//   description: Return response as a CollectionPage.
//   in: query
//   default: false
// - name: min_id
//   type: string
//   description: Minimum ID of the next status, used for paging.
//   in: query
// - name: max_id
//   type: string
//   description: Maximum ID of the next status, used for paging.
//   in: query
//
// responses:
//   '200':
//      in: body
//      schema:
//        "$ref": "#/definitions/swaggerCollection"
//   '400':
//      description: bad request
//   '401':
//      description: unauthorized
//   '403':
//      description: forbidden
//   '404':
//      description: not found
func (m *Module) OutboxGETHandler(c *gin.Context) {
	// usernames on our instance are always lowercase
	requestedUsername := strings.ToLower(c.Param(UsernameKey))
	if requestedUsername == "" {
		err := errors.New("no username specified in request")
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	format, err := api.NegotiateAccept(c, api.HTMLOrActivityPubHeaders...)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if format == string(api.TextHTML) {
		// redirect to the user's profile
		c.Redirect(http.StatusSeeOther, "/@"+requestedUsername)
	}

	var page bool
	if pageString := c.Query(PageKey); pageString != "" {
		i, err := strconv.ParseBool(pageString)
		if err != nil {
			err := fmt.Errorf("error parsing %s: %s", PageKey, err)
			api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
			return
		}
		page = i
	}

	minID := ""
	minIDString := c.Query(MinIDKey)
	if minIDString != "" {
		minID = minIDString
	}

	maxID := ""
	maxIDString := c.Query(MaxIDKey)
	if maxIDString != "" {
		maxID = maxIDString
	}

	resp, errWithCode := m.processor.GetFediOutbox(transferContext(c), requestedUsername, page, maxID, minID, c.Request.URL)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	b, err := json.Marshal(resp)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorInternalError(err), m.processor.InstanceGet)
		return
	}

	c.Data(http.StatusOK, format, b)
}
