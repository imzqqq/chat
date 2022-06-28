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

package account

import (
	"errors"
	"fmt"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// AccountStatusesGETHandler swagger:operation GET /api/v1/accounts/{id}/statuses accountStatuses
//
// See statuses posted by the requested account.
//
// The statuses will be returned in descending chronological order (newest first), with sequential IDs (bigger = newer).
//
// ---
// tags:
// - accounts
//
// produces:
// - application/json
//
// parameters:
// - name: id
//   type: string
//   description: Account ID.
//   in: path
//   required: true
// - name: limit
//   type: integer
//   description: Number of statuses to return.
//   default: 30
//   in: query
//   required: false
// - name: exclude_replies
//   type: boolean
//   description: Exclude statuses that are a reply to another status.
//   default: false
//   in: query
//   required: false
// - name: exclude_reblogs
//   type: boolean
//   description: Exclude statuses that are a reblog/boost of another status.
//   default: false
//   in: query
//   required: false
// - name: max_id
//   type: string
//   description: |-
//     Return only statuses *OLDER* than the given max status ID.
//     The status with the specified ID will not be included in the response.
//   in: query
// - name: min_id
//   type: string
//   description: |-
//     Return only statuses *NEWER* than the given min status ID.
//     The status with the specified ID will not be included in the response.
//   in: query
//   required: false
// - name: pinned_only
//   type: boolean
//   description: Show only pinned statuses. In other words, exclude statuses that are not pinned to the given account ID.
//   default: false
//   in: query
//   required: false
// - name: only_media
//   type: boolean
//   description: Show only statuses with media attachments.
//   default: false
//   in: query
//   required: false
// - name: only_public
//   type: boolean
//   description: Show only statuses with a privacy setting of 'public'.
//   default: false
//   in: query
//   required: false
//
// security:
// - OAuth2 Bearer:
//   - read:accounts
//
// responses:
//   '200':
//     name: statuses
//     description: Array of statuses.
//     schema:
//       type: array
//       items:
//         "$ref": "#/definitions/status"
//   '400':
//      description: bad request
//   '401':
//      description: unauthorized
//   '404':
//      description: not found
//   '406':
//      description: not acceptable
//   '500':
//      description: internal server error
func (m *Module) AccountStatusesGETHandler(c *gin.Context) {
	authed, err := oauth.Authed(c, false, false, false, false)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorUnauthorized(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	targetAcctID := c.Param(IDKey)
	if targetAcctID == "" {
		err := errors.New("no account id specified")
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	limit := 30
	limitString := c.Query(LimitKey)
	if limitString != "" {
		i, err := strconv.ParseInt(limitString, 10, 64)
		if err != nil {
			err := fmt.Errorf("error parsing %s: %s", LimitKey, err)
			api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
			return
		}
		limit = int(i)
	}

	excludeReplies := false
	excludeRepliesString := c.Query(ExcludeRepliesKey)
	if excludeRepliesString != "" {
		i, err := strconv.ParseBool(excludeRepliesString)
		if err != nil {
			err := fmt.Errorf("error parsing %s: %s", ExcludeRepliesKey, err)
			api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
			return
		}
		excludeReplies = i
	}

	excludeReblogs := false
	excludeReblogsString := c.Query(ExcludeReblogsKey)
	if excludeReblogsString != "" {
		i, err := strconv.ParseBool(excludeReblogsString)
		if err != nil {
			err := fmt.Errorf("error parsing %s: %s", ExcludeReblogsKey, err)
			api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
			return
		}
		excludeReblogs = i
	}

	maxID := ""
	maxIDString := c.Query(MaxIDKey)
	if maxIDString != "" {
		maxID = maxIDString
	}

	minID := ""
	minIDString := c.Query(MinIDKey)
	if minIDString != "" {
		minID = minIDString
	}

	pinnedOnly := false
	pinnedString := c.Query(PinnedKey)
	if pinnedString != "" {
		i, err := strconv.ParseBool(pinnedString)
		if err != nil {
			err := fmt.Errorf("error parsing %s: %s", PinnedKey, err)
			api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
			return
		}
		pinnedOnly = i
	}

	mediaOnly := false
	mediaOnlyString := c.Query(OnlyMediaKey)
	if mediaOnlyString != "" {
		i, err := strconv.ParseBool(mediaOnlyString)
		if err != nil {
			err := fmt.Errorf("error parsing %s: %s", OnlyMediaKey, err)
			api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
			return
		}
		mediaOnly = i
	}

	publicOnly := false
	publicOnlyString := c.Query(OnlyPublicKey)
	if publicOnlyString != "" {
		i, err := strconv.ParseBool(publicOnlyString)
		if err != nil {
			err := fmt.Errorf("error parsing %s: %s", OnlyPublicKey, err)
			api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
			return
		}
		publicOnly = i
	}

	resp, errWithCode := m.processor.AccountStatusesGet(c.Request.Context(), authed, targetAcctID, limit, excludeReplies, excludeReblogs, maxID, minID, pinnedOnly, mediaOnly, publicOnly)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	if resp.LinkHeader != "" {
		c.Header("Link", resp.LinkHeader)
	}
	c.JSON(http.StatusOK, resp.Items)
}
