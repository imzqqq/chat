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

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/api/model"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// AccountActionPOSTHandler swagger:operation POST /api/v1/admin/accounts/{id}/action adminAccountAction
//
// Perform an admin action on an account.
//
// ---
// tags:
// - admin
//
// consumes:
// - multipart/form-data
//
// produces:
// - application/json
//
// parameters:
// - name: id
//   required: true
//   in: path
//   description: ID of the account.
//   type: string
// - name: type
//   in: formData
//   description: |-
//     Type of action to be taken. One of: disable, silence, suspend.
//   type: string
//   required: true
// - name: text
//   in: formData
//   description: Optional text describing why this action was taken.
//   type: string
//
// security:
// - OAuth2 Bearer:
//   - admin
//
// responses:
//   '200':
//     description: OK
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
func (m *Module) AccountActionPOSTHandler(c *gin.Context) {
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

	form := &model.AdminAccountActionRequest{}
	if err := c.ShouldBind(form); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if form.Type == "" {
		err := errors.New("no type specified")
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	targetAcctID := c.Param(IDKey)
	if targetAcctID == "" {
		err := errors.New("no account id specified")
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}
	form.TargetAccountID = targetAcctID

	if errWithCode := m.processor.AdminAccountAction(c.Request.Context(), authed, form); errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "OK"})
}
