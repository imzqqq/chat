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

package nodeinfo

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
)

// NodeInfoWellKnownGETHandler swagger:operation GET /.well-known/nodeinfo nodeInfoWellKnownGet
//
// Directs callers to /nodeinfo/2.0.
//
// eg. `{"links":[{"rel":"http://nodeinfo.diaspora.software/ns/schema/2.0","href":"http://example.org/nodeinfo/2.0"}]}`
// See: https://nodeinfo.diaspora.software/protocol.html
//
// ---
// tags:
// - nodeinfo
//
// produces:
// - application/json
//
// responses:
//   '200':
//     schema:
//       "$ref": "#/definitions/wellKnownResponse"
func (m *Module) NodeInfoWellKnownGETHandler(c *gin.Context) {
	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	niRel, errWithCode := m.processor.GetNodeInfoRel(c.Request.Context(), c.Request)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	c.JSON(http.StatusOK, niRel)
}
