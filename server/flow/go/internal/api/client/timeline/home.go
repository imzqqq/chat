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

package timeline

import (
	"net/http"
	"strconv"

	"github.com/sirupsen/logrus"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// HomeTimelineGETHandler swagger:operation GET /api/v1/timelines/home homeTimeline
//
// See statuses/posts by accounts you follow.
//
// The statuses will be returned in descending chronological order (newest first), with sequential IDs (bigger = newer).
//
// The returned Link header can be used to generate the previous and next queries when scrolling up or down a timeline.
//
// Example:
//
// ```
// <https://example.org/api/v1/timelines/home?limit=20&max_id=01FC3GSQ8A3MMJ43BPZSGEG29M>; rel="next", <https://example.org/api/v1/timelines/home?limit=20&min_id=01FC3KJW2GYXSDDRA6RWNDM46M>; rel="prev"
// ````
//
// ---
// tags:
// - timelines
//
// produces:
// - application/json
//
// parameters:
// - name: max_id
//   type: string
//   description: |-
//     Return only statuses *OLDER* than the given max status ID.
//     The status with the specified ID will not be included in the response.
//   in: query
//   required: false
// - name: since_id
//   type: string
//   description: |-
//     Return only statuses *NEWER* than the given since status ID.
//     The status with the specified ID will not be included in the response.
//   in: query
// - name: min_id
//   type: string
//   description: |-
//     Return only statuses *NEWER* than the given since status ID.
//     The status with the specified ID will not be included in the response.
//   in: query
//   required: false
// - name: limit
//   type: integer
//   description: Number of statuses to return.
//   default: 20
//   in: query
//   required: false
// - name: local
//   type: boolean
//   description: Show only statuses posted by local accounts.
//   default: false
//   in: query
//   required: false
//
// security:
// - OAuth2 Bearer:
//   - read:statuses
//
// responses:
//   '200':
//     name: statuses
//     description: Array of statuses.
//     schema:
//       type: array
//       items:
//         "$ref": "#/definitions/status"
//     headers:
//       Link:
//         type: string
//         description: Links to the next and previous queries.
//   '401':
//      description: unauthorized
//   '400':
//      description: bad request
func (m *Module) HomeTimelineGETHandler(c *gin.Context) {
	l := logrus.WithField("func", "HomeTimelineGETHandler")

	authed, err := oauth.Authed(c, true, true, true, true)
	if err != nil {
		l.Debugf("error authing: %s", err)
		c.JSON(http.StatusUnauthorized, gin.H{"error": "unauthorized"})
		return
	}

	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		c.JSON(http.StatusNotAcceptable, gin.H{"error": err.Error()})
		return
	}

	maxID := ""
	maxIDString := c.Query(MaxIDKey)
	if maxIDString != "" {
		maxID = maxIDString
	}

	sinceID := ""
	sinceIDString := c.Query(SinceIDKey)
	if sinceIDString != "" {
		sinceID = sinceIDString
	}

	minID := ""
	minIDString := c.Query(MinIDKey)
	if minIDString != "" {
		minID = minIDString
	}

	limit := 20
	limitString := c.Query(LimitKey)
	if limitString != "" {
		i, err := strconv.ParseInt(limitString, 10, 64)
		if err != nil {
			l.Debugf("error parsing limit string: %s", err)
			c.JSON(http.StatusBadRequest, gin.H{"error": "couldn't parse limit query param"})
			return
		}
		limit = int(i)
	}

	local := false
	localString := c.Query(LocalKey)
	if localString != "" {
		i, err := strconv.ParseBool(localString)
		if err != nil {
			l.Debugf("error parsing local string: %s", err)
			c.JSON(http.StatusBadRequest, gin.H{"error": "couldn't parse local query param"})
			return
		}
		local = i
	}

	resp, errWithCode := m.processor.HomeTimelineGet(c.Request.Context(), authed, maxID, sinceID, minID, limit, local)
	if errWithCode != nil {
		l.Debugf("error from processor HomeTimelineGet: %s", errWithCode)
		c.JSON(errWithCode.Code(), gin.H{"error": errWithCode.Safe()})
		return
	}

	if resp.LinkHeader != "" {
		c.Header("Link", resp.LinkHeader)
	}
	c.JSON(http.StatusOK, resp.Statuses)
}
