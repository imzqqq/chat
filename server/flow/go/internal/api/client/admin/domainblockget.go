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

package admin

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

// DomainBlockGETHandler swagger:operation GET /api/v1/admin/domain_blocks/{id} domainBlockGet
//
// View domain block with the given ID.
//
// ---
// tags:
// - admin
//
// produces:
// - application/json
//
// parameters:
// - name: id
//   type: string
//   description: The id of the domain block.
//   in: path
//   required: true
//
// security:
// - OAuth2 Bearer:
//   - admin
//
// responses:
//   '200':
//     description: The requested domain block.
//     schema:
//       "$ref": "#/definitions/domainBlock"
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
func (m *Module) DomainBlockGETHandler(c *gin.Context) {
	authed, err := oauth.Authed(c, true, true, true, true)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorUnauthorized(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if !authed.User.Admin {
		err := fmt.Errorf("user %s not an admin", authed.User.ID)
		api.ErrorHandler(c, gtserror.NewErrorForbidden(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	domainBlockID := c.Param(IDKey)
	if domainBlockID == "" {
		err := errors.New("no domain block id specified")
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	export := false
	exportString := c.Query(ExportQueryKey)
	if exportString != "" {
		i, err := strconv.ParseBool(exportString)
		if err != nil {
			err := fmt.Errorf("error parsing %s: %s", ExportQueryKey, err)
			api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
			return
		}
		export = i
	}

	domainBlock, errWithCode := m.processor.AdminDomainBlockGet(c.Request.Context(), authed, domainBlockID, export)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	c.JSON(http.StatusOK, domainBlock)
}
