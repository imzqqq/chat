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

package user

import (
	"encoding/json"
	"errors"
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
)

// PublicKeyGETHandler should be served at eg https://example.org/users/:username/main-key.
//
// The goal here is to return a MINIMAL activitypub representation of an account
// in the form of a vocab.ActivityStreamsPerson. The account will only contain the id,
// public key, username, and type of the account.
func (m *Module) PublicKeyGETHandler(c *gin.Context) {
	// usernames on our instance are always lowercase
	requestedUsername := strings.ToLower(c.Param(UsernameKey))
	if requestedUsername == "" {
		err := errors.New("no username specified in request")
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	format, err := api.NegotiateAccept(c, api.HTMLOrActivityPubHeaders...)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if format == string(api.TextHTML) {
		// redirect to the user's profile
		c.Redirect(http.StatusSeeOther, "/@"+requestedUsername)
	}

	resp, errWithCode := m.processor.GetFediUser(transferContext(c), requestedUsername, c.Request.URL)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	b, err := json.Marshal(resp)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorInternalError(err), m.processor.InstanceGet)
		return
	}

	c.Data(http.StatusOK, format, b)
}
