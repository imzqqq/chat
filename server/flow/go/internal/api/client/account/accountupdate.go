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
	"github.com/superseriousbusiness/gotosocial/internal/api/model"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

// AccountUpdateCredentialsPATCHHandler swagger:operation PATCH /api/v1/accounts/update_credentials accountUpdate
//
// Update your account.
//
// ---
// tags:
// - accounts
//
// consumes:
// - multipart/form-data
//
// produces:
// - application/json
//
// parameters:
// - name: discoverable
//   in: formData
//   description: Account should be made discoverable and shown in the profile directory (if enabled).
//   type: boolean
// - name: bot
//   in: formData
//   description: Account is flagged as a bot.
//   type: boolean
// - name: display_name
//   in: formData
//   description: The display name to use for the account.
//   type: string
//   allowEmptyValue: true
// - name: note
//   in: formData
//   description: Bio/description of this account.
//   type: string
//   allowEmptyValue: true
// - name: avatar
//   in: formData
//   description: Avatar of the user.
//   type: file
// - name: header
//   in: formData
//   description: Header of the user.
//   type: file
// - name: locked
//   in: formData
//   description: Require manual approval of follow requests.
//   type: boolean
// - name: source[privacy]
//   in: formData
//   description: Default post privacy for authored statuses.
//   type: string
// - name: source[sensitive]
//   in: formData
//   description: Mark authored statuses as sensitive by default.
//   type: boolean
// - name: source[language]
//   in: formData
//   description: Default language to use for authored statuses (ISO 6391).
//   type: string
//
// security:
// - OAuth2 Bearer:
//   - write:accounts
//
// responses:
//   '200':
//     description: "The newly updated account."
//     schema:
//       "$ref": "#/definitions/account"
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
func (m *Module) AccountUpdateCredentialsPATCHHandler(c *gin.Context) {
	authed, err := oauth.Authed(c, true, true, true, true)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorUnauthorized(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	form, err := parseUpdateAccountForm(c)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	acctSensitive, errWithCode := m.processor.AccountUpdate(c.Request.Context(), authed, form)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	c.JSON(http.StatusOK, acctSensitive)
}

func parseUpdateAccountForm(c *gin.Context) (*model.UpdateCredentialsRequest, error) {
	form := &model.UpdateCredentialsRequest{
		Source: &model.UpdateSource{},
	}

	if err := c.ShouldBind(&form); err != nil {
		return nil, fmt.Errorf("could not parse form from request: %s", err)
	}

	if form == nil ||
		(form.Discoverable == nil &&
			form.Bot == nil &&
			form.DisplayName == nil &&
			form.Note == nil &&
			form.Avatar == nil &&
			form.Header == nil &&
			form.Locked == nil &&
			form.Source.Privacy == nil &&
			form.Source.Sensitive == nil &&
			form.Source.Language == nil &&
			form.FieldsAttributes == nil) {
		return nil, errors.New("empty form submitted")
	}

	// parse source field-by-field
	sourceMap := c.PostFormMap("source")

	if privacy, ok := sourceMap["privacy"]; ok {
		form.Source.Privacy = &privacy
	}

	if sensitive, ok := sourceMap["sensitive"]; ok {
		sensitiveBool, err := strconv.ParseBool(sensitive)
		if err != nil {
			return nil, fmt.Errorf("error parsing form source[sensitive]: %s", err)
		}
		form.Source.Sensitive = &sensitiveBool
	}

	if language, ok := sourceMap["language"]; ok {
		form.Source.Language = &language
	}

	return form, nil
}
