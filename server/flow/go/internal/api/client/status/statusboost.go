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

package status

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// StatusBoostPOSTHandler swagger:operation POST /api/v1/statuses/{id}/reblog statusReblog
//
// Reblog/boost status with the given ID.
//
// If the target status is rebloggable/boostable, it will be shared with your followers.
// This is equivalent to an activitypub 'announce' activity.
//
// ---
// tags:
// - statuses
//
// produces:
// - application/json
//
// parameters:
// - name: id
//   type: string
//   description: Target status ID.
//   in: path
//   required: true
//
// security:
// - OAuth2 Bearer:
//   - write:statuses
//
// responses:
//   '200':
//     name: status
//     description: The boost of the status.
//     schema:
//       "$ref": "#/definitions/status"
//   '400':
//      description: bad request
//   '401':
//      description: unauthorized
//   '403':
//      description: forbidden
//   '404':
//      description: not found
func (m *Module) StatusBoostPOSTHandler(c *gin.Context) {
	l := logrus.WithFields(logrus.Fields{
		"func":        "StatusBoostPOSTHandler",
		"request_uri": c.Request.RequestURI,
		"user_agent":  c.Request.UserAgent(),
		"origin_ip":   c.ClientIP(),
	})
	l.Debugf("entering function")

	authed, err := oauth.Authed(c, true, true, true, true)
	if err != nil {
		l.Debug("not authed so can't boost status")
		c.JSON(http.StatusUnauthorized, gin.H{"error": "not authorized"})
		return
	}

	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		c.JSON(http.StatusNotAcceptable, gin.H{"error": err.Error()})
		return
	}

	targetStatusID := c.Param(IDKey)
	if targetStatusID == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "no status id provided"})
		return
	}

	apiStatus, errWithCode := m.processor.StatusBoost(c.Request.Context(), authed, targetStatusID)
	if errWithCode != nil {
		l.Debugf("error processing status boost: %s", errWithCode.Error())
		c.JSON(errWithCode.Code(), gin.H{"error": errWithCode.Safe()})
		return
	}

	c.JSON(http.StatusOK, apiStatus)
}
