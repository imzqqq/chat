/*
   GoToSocial
   Copyright (C) 2021 GoToSocial Authors admin@gotosocial.org

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
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// AccountUnblockPOSTHandler swagger:operation POST /api/v1/accounts/{id}/unblock accountUnblock
//
// Unblock account with ID.
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
//   description: The id of the account to unblock.
//   in: path
//   required: true
//
// security:
// - OAuth2 Bearer:
//   - write:blocks
//
// responses:
//   '200':
//     name: account relationship
//     description: Your relationship to this account.
//     schema:
//       "$ref": "#/definitions/accountRelationship"
//   '401':
//      description: unauthorized
//   '400':
//      description: bad request
//   '404':
//      description: not found
func (m *Module) AccountUnblockPOSTHandler(c *gin.Context) {
	authed, err := oauth.Authed(c, true, true, true, true)
	if err != nil {
		c.JSON(http.StatusUnauthorized, gin.H{"error": "unauthorized"})
		return
	}

	targetAcctID := c.Param(IDKey)
	if targetAcctID == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "no account id specified"})
		return
	}

	relationship, errWithCode := m.processor.AccountBlockRemove(c.Request.Context(), authed, targetAcctID)
	if errWithCode != nil {
		c.JSON(errWithCode.Code(), gin.H{"error": errWithCode.Safe()})
		return
	}

	c.JSON(http.StatusOK, relationship)
}
