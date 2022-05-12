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

package followrequest

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// FollowRequestRejectPOSTHandler swagger:operation POST /api/v1/follow_requests/{account_id}/reject rejectFollowRequest
//
// Reject/deny follow request from the given account ID.
//
// ---
// tags:
// - follow_requests
//
// produces:
// - application/json
//
// parameters:
// - name: account_id
//   type: string
//   description: ID of the account requesting to follow you.
//   in: path
//   required: true
//
// security:
// - OAuth2 Bearer:
//   - write:follows
//
// responses:
//   '200':
//     name: account relationship
//     description: Your relationship to this account.
//     schema:
//       "$ref": "#/definitions/accountRelationship"
//   '400':
//      description: bad request
//   '401':
//      description: unauthorized
//   '403':
//      description: forbidden
//   '404':
//      description: not found
//   '500':
//      description: internal server error
func (m *Module) FollowRequestRejectPOSTHandler(c *gin.Context) {
	l := logrus.WithField("func", "FollowRequestRejectPOSTHandler")

	authed, err := oauth.Authed(c, true, true, true, true)
	if err != nil {
		l.Debugf("couldn't auth: %s", err)
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
		return
	}

	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		c.JSON(http.StatusNotAcceptable, gin.H{"error": err.Error()})
		return
	}

	if authed.User.Disabled || !authed.User.Approved || !authed.Account.SuspendedAt.IsZero() {
		l.Debugf("couldn't auth: %s", err)
		c.JSON(http.StatusForbidden, gin.H{"error": "account is disabled, not yet approved, or suspended"})
		return
	}

	originAccountID := c.Param(IDKey)
	if originAccountID == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "no follow request origin account id provided"})
		return
	}

	relationship, errWithCode := m.processor.FollowRequestReject(c.Request.Context(), authed, originAccountID)
	if errWithCode != nil {
		l.Debug(errWithCode.Error())
		c.JSON(errWithCode.Code(), gin.H{"error": errWithCode.Safe()})
		return
	}

	c.JSON(http.StatusOK, relationship)
}
