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

package web

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"math/rand"
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/ap"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	apimodel "github.com/superseriousbusiness/gotosocial/internal/api/model"
	"github.com/superseriousbusiness/gotosocial/internal/config"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

var randAvatars = make(map[string]string)

func (m *Module) ensureAvatar(status apimodel.Status) {
	if status.Account.Avatar == "" && len(m.defaultAvatars) > 0 {
		avatar, ok := randAvatars[status.Account.ID]
		if !ok {
			//nolint:gosec
			randomIndex := rand.Intn(len(m.defaultAvatars))
			avatar = m.defaultAvatars[randomIndex]
			randAvatars[status.Account.ID] = avatar
		}
		status.Account.Avatar = avatar
	}
}

func (m *Module) threadGETHandler(c *gin.Context) {
	ctx := c.Request.Context()

	authed, err := oauth.Authed(c, false, false, false, false)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorUnauthorized(err, err.Error()), m.processor.InstanceGet)
		return
	}

	// usernames on our instance will always be lowercase
	username := strings.ToLower(c.Param(usernameKey))
	if username == "" {
		err := errors.New("no account username specified")
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	// status ids will always be uppercase
	statusID := strings.ToUpper(c.Param(statusIDKey))
	if statusID == "" {
		err := errors.New("no status id specified")
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	host := config.GetHost()
	instance, err := m.processor.InstanceGet(ctx, host)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorInternalError(err), m.processor.InstanceGet)
		return
	}

	instanceGet := func(ctx context.Context, domain string) (*apimodel.Instance, gtserror.WithCode) {
		return instance, nil
	}

	// do this check to make sure the status is actually from a local account,
	// we shouldn't render threads from statuses that don't belong to us!
	if _, errWithCode := m.processor.AccountGetLocalByUsername(ctx, authed, username); errWithCode != nil {
		api.ErrorHandler(c, errWithCode, instanceGet)
		return
	}

	status, errWithCode := m.processor.StatusGet(ctx, authed, statusID)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, instanceGet)
		return
	}

	if !strings.EqualFold(username, status.Account.Username) {
		err := gtserror.NewErrorNotFound(errors.New("path username not equal to status author username"))
		api.ErrorHandler(c, gtserror.NewErrorNotFound(err), instanceGet)
		return
	}

	// if we're getting an AP request on this endpoint we
	// should render the status's AP representation instead
	accept := c.NegotiateFormat(string(api.TextHTML), string(api.AppActivityJSON), string(api.AppActivityLDJSON))
	if accept == string(api.AppActivityJSON) || accept == string(api.AppActivityLDJSON) {
		m.returnAPStatus(ctx, c, username, statusID, accept)
		return
	}

	context, errWithCode := m.processor.StatusGetContext(ctx, authed, statusID)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, instanceGet)
		return
	}

	m.ensureAvatar(*status)

	for _, status := range context.Descendants {
		m.ensureAvatar(status)
	}

	for _, status := range context.Ancestors {
		m.ensureAvatar(status)
	}

	c.HTML(http.StatusOK, "thread.tmpl", gin.H{
		"instance": instance,
		"status":   status,
		"context":  context,
		"stylesheets": []string{
			"/assets/Fork-Awesome/css/fork-awesome.min.css",
			"/assets/dist/status.css",
		},
		"javascript": []string{
			"/assets/dist/frontend.js",
		},
	})
}

func (m *Module) returnAPStatus(ctx context.Context, c *gin.Context, username string, statusID string, accept string) {
	verifier, signed := c.Get(string(ap.ContextRequestingPublicKeyVerifier))
	if signed {
		ctx = context.WithValue(ctx, ap.ContextRequestingPublicKeyVerifier, verifier)
	}

	signature, signed := c.Get(string(ap.ContextRequestingPublicKeySignature))
	if signed {
		ctx = context.WithValue(ctx, ap.ContextRequestingPublicKeySignature, signature)
	}

	status, errWithCode := m.processor.GetFediStatus(ctx, username, statusID, c.Request.URL)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	b, mErr := json.Marshal(status)
	if mErr != nil {
		err := fmt.Errorf("could not marshal json: %s", mErr)
		api.ErrorHandler(c, gtserror.NewErrorInternalError(err), m.processor.InstanceGet)
		return
	}

	c.Data(http.StatusOK, accept, b)
}
