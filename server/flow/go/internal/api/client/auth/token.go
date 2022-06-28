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

package auth

import (
	"net/http"
	"net/url"

	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"

	"github.com/gin-gonic/gin"
)

type tokenRequestForm struct {
	GrantType    *string `form:"grant_type" json:"grant_type" xml:"grant_type"`
	Code         *string `form:"code" json:"code" xml:"code"`
	RedirectURI  *string `form:"redirect_uri" json:"redirect_uri" xml:"redirect_uri"`
	ClientID     *string `form:"client_id" json:"client_id" xml:"client_id"`
	ClientSecret *string `form:"client_secret" json:"client_secret" xml:"client_secret"`
	Scope        *string `form:"scope" json:"scope" xml:"scope"`
}

// TokenPOSTHandler should be served as a POST at https://example.org/oauth/token
// The idea here is to serve an oauth access token to a user, which can be used for authorizing against non-public APIs.
func (m *Module) TokenPOSTHandler(c *gin.Context) {
	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	help := []string{}

	form := &tokenRequestForm{}
	if err := c.ShouldBind(form); err != nil {
		api.OAuthErrorHandler(c, gtserror.NewErrorBadRequest(oauth.InvalidRequest(), err.Error()))
		return
	}

	c.Request.Form = url.Values{}

	var grantType string
	if form.GrantType != nil {
		grantType = *form.GrantType
		c.Request.Form.Set("grant_type", grantType)
	} else {
		help = append(help, "grant_type was not set in the token request form, but must be set to authorization_code or client_credentials")
	}

	if form.ClientID != nil {
		c.Request.Form.Set("client_id", *form.ClientID)
	} else {
		help = append(help, "client_id was not set in the token request form")
	}

	if form.ClientSecret != nil {
		c.Request.Form.Set("client_secret", *form.ClientSecret)
	} else {
		help = append(help, "client_secret was not set in the token request form")
	}

	if form.RedirectURI != nil {
		c.Request.Form.Set("redirect_uri", *form.RedirectURI)
	} else {
		help = append(help, "redirect_uri was not set in the token request form")
	}

	var code string
	if form.Code != nil {
		if grantType != "authorization_code" {
			help = append(help, "a code was provided in the token request form, but grant_type was not set to authorization_code")
		} else {
			code = *form.Code
			c.Request.Form.Set("code", code)
		}
	} else if grantType == "authorization_code" {
		help = append(help, "code was not set in the token request form, but must be set since grant_type is authorization_code")
	}

	if form.Scope != nil {
		c.Request.Form.Set("scope", *form.Scope)
	}

	if len(help) != 0 {
		api.OAuthErrorHandler(c, gtserror.NewErrorBadRequest(oauth.InvalidRequest(), help...))
		return
	}

	token, errWithCode := m.processor.OAuthHandleTokenRequest(c.Request)
	if errWithCode != nil {
		api.OAuthErrorHandler(c, errWithCode)
		return
	}

	c.Header("Cache-Control", "no-store")
	c.Header("Pragma", "no-cache")
	c.JSON(http.StatusOK, token)
}
