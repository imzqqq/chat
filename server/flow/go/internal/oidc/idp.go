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

package oidc

import (
	"context"
	"fmt"

	"github.com/coreos/go-oidc/v3/oidc"
	"github.com/superseriousbusiness/gotosocial/internal/config"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"golang.org/x/oauth2"
)

const (
	// CallbackPath is the API path for receiving callback tokens from external OIDC providers
	CallbackPath = "/auth/callback"
)

// IDP contains logic for parsing an OIDC access code into a set of claims by calling an external OIDC provider.
type IDP interface {
	// HandleCallback accepts a context (pass the context from the http.Request), and an oauth2 code as returned from a successful
	// login through an OIDC provider. It uses the code to request a token from the OIDC provider, which should contain an id_token
	// with a set of claims.
	//
	// Note that this function *does not* verify state. That should be handled by the caller *before* this function is called.
	HandleCallback(ctx context.Context, code string) (*Claims, gtserror.WithCode)
	// AuthCodeURL returns the proper redirect URL for this IDP, for redirecting requesters to the correct OIDC endpoint.
	AuthCodeURL(state string) string
}

type idp struct {
	oauth2Config oauth2.Config
	provider     *oidc.Provider
	oidcConf     *oidc.Config
}

// NewIDP returns a new IDP configured with the given config.
// If the passed config contains a nil value for the OIDCConfig, or OIDCConfig.Enabled
// is set to false, then nil, nil will be returned. If OIDCConfig.Enabled is true,
// then the other OIDC config fields must also be set.
func NewIDP(ctx context.Context) (IDP, error) {
	if !config.GetOIDCEnabled() {
		// oidc isn't enabled so we don't need to do anything
		return nil, nil
	}

	// validate config fields
	idpName := config.GetOIDCIdpName()
	if idpName == "" {
		return nil, fmt.Errorf("not set: IDPName")
	}

	issuer := config.GetOIDCIssuer()
	if issuer == "" {
		return nil, fmt.Errorf("not set: Issuer")
	}

	clientID := config.GetOIDCClientID()
	if clientID == "" {
		return nil, fmt.Errorf("not set: ClientID")
	}

	clientSecret := config.GetOIDCClientSecret()
	if clientSecret == "" {
		return nil, fmt.Errorf("not set: ClientSecret")
	}

	scopes := config.GetOIDCScopes()
	if len(scopes) == 0 {
		return nil, fmt.Errorf("not set: Scopes")
	}

	provider, err := oidc.NewProvider(ctx, issuer)
	if err != nil {
		return nil, err
	}

	protocol := config.GetProtocol()
	host := config.GetHost()

	oauth2Config := oauth2.Config{
		// client_id and client_secret of the client.
		ClientID:     clientID,
		ClientSecret: clientSecret,

		// The redirectURL.
		RedirectURL: fmt.Sprintf("%s://%s%s", protocol, host, CallbackPath),

		// Discovery returns the OAuth2 endpoints.
		Endpoint: provider.Endpoint(),

		// "openid" is a required scope for OpenID Connect flows.
		//
		// Other scopes, such as "groups" can be requested.
		Scopes: scopes,
	}

	// create a config for verifier creation
	oidcConf := &oidc.Config{
		ClientID: clientID,
	}

	if config.GetOIDCSkipVerification() {
		oidcConf.SkipClientIDCheck = true
		oidcConf.SkipExpiryCheck = true
		oidcConf.SkipIssuerCheck = true
	}

	return &idp{
		oauth2Config: oauth2Config,
		oidcConf:     oidcConf,
		provider:     provider,
	}, nil
}
