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
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// AccountFollowingGETHandler swagger:operation GET /api/v1/accounts/{id}/following accountFollowing
//
// See accounts followed by given account id.
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
//
// security:
// - OAuth2 Bearer:
//   - read:accounts
//
// responses:
//   '200':
//     name: accounts
//     description: Array of accounts that are followed by this account.
//     schema:
//       type: array
//       items:
//         "$ref": "#/definitions/account"
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
func (m *Module) AccountFollowingGETHandler(c *gin.Context) {
	authed, err := oauth.Authed(c, true, true, true, true)
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

	following, errWithCode := m.processor.AccountFollowingGet(c.Request.Context(), authed, targetAcctID)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	c.JSON(http.StatusOK, following)
}
