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

package transport

import (
	"context"
	"crypto/rsa"
	"crypto/x509"
	"encoding/json"
	"fmt"
	"net/url"
	"runtime/debug"
	"time"

	"codeberg.org/gruf/go-byteutil"
	"codeberg.org/gruf/go-cache/v2"
	"github.com/sirupsen/logrus"
	"github.com/spf13/viper"
	"github.com/superseriousbusiness/activity/pub"
	"github.com/superseriousbusiness/activity/streams"
	"github.com/superseriousbusiness/gotosocial/internal/config"
	"github.com/superseriousbusiness/gotosocial/internal/db"
	"github.com/superseriousbusiness/gotosocial/internal/federation/federatingdb"
)

// Controller generates transports for use in making federation requests to other servers.
type Controller interface {
	// NewTransport returns an http signature transport with the given public key ID (URL location of pubkey), and the given private key.
	NewTransport(pubKeyID string, privkey *rsa.PrivateKey) (Transport, error)

	// NewTransportForUsername searches for account with username, and returns result of .NewTransport().
	NewTransportForUsername(ctx context.Context, username string) (Transport, error)
}

type controller struct {
	db        db.DB
	fedDB     federatingdb.DB
	clock     pub.Clock
	client    pub.HttpClient
	cache     cache.Cache[string, *transport]
	userAgent string
}

// NewController returns an implementation of the Controller interface for creating new transports
func NewController(db db.DB, federatingDB federatingdb.DB, clock pub.Clock, client pub.HttpClient) Controller {
	applicationName := viper.GetString(config.Keys.ApplicationName)
	host := viper.GetString(config.Keys.Host)

	// Determine build information
	build, _ := debug.ReadBuildInfo()

	c := &controller{
		db:        db,
		fedDB:     federatingDB,
		clock:     clock,
		client:    client,
		cache:     cache.New[string, *transport](),
		userAgent: fmt.Sprintf("%s; %s (gofed/activity gotosocial-%s)", applicationName, host, build.Main.Version),
	}

	// Transport cache has TTL=1hr freq=1m
	c.cache.SetTTL(time.Hour, false)
	if !c.cache.Start(time.Minute) {
		logrus.Panic("failed to start transport controller cache")
	}

	return c
}

func (c *controller) NewTransport(pubKeyID string, privkey *rsa.PrivateKey) (Transport, error) {
	// Generate public key string for cache key
	//
	// NOTE: it is safe to use the public key as the cache
	// key here as we are generating it ourselves from the
	// private key. If we were simply using a public key
	// provided as argument that would absolutely NOT be safe.
	pubStr := privkeyToPublicStr(privkey)

	// First check for cached transport
	transp, ok := c.cache.Get(pubStr)
	if ok {
		return transp, nil
	}

	// Create the transport
	transp = &transport{
		controller: c,
		pubKeyID:   pubKeyID,
		privkey:    privkey,
	}

	// Cache this transport under pubkey
	if !c.cache.Put(pubStr, transp) {
		var cached *transport

		cached, ok = c.cache.Get(pubStr)
		if !ok {
			// Some ridiculous race cond.
			c.cache.Set(pubStr, transp)
		} else {
			// Use already cached
			transp = cached
		}
	}

	return transp, nil
}

func (c *controller) NewTransportForUsername(ctx context.Context, username string) (Transport, error) {
	// We need an account to use to create a transport for dereferecing something.
	// If a username has been given, we can fetch the account with that username and use it.
	// Otherwise, we can take the instance account and use those credentials to make the request.
	var u string
	if username == "" {
		u = viper.GetString(config.Keys.Host)
	} else {
		u = username
	}

	ourAccount, err := c.db.GetLocalAccountByUsername(ctx, u)
	if err != nil {
		return nil, fmt.Errorf("error getting account %s from db: %s", username, err)
	}

	transport, err := c.NewTransport(ourAccount.PublicKeyURI, ourAccount.PrivateKey)
	if err != nil {
		return nil, fmt.Errorf("error creating transport for user %s: %s", username, err)
	}
	return transport, nil
}

// dereferenceLocalFollowers is a shortcut to dereference followers of an
// account on this instance, without making any external api/http calls.
//
// It is passed to new transports, and should only be invoked when the iri.Host == this host.
func (c *controller) dereferenceLocalFollowers(ctx context.Context, iri *url.URL) ([]byte, error) {
	followers, err := c.fedDB.Followers(ctx, iri)
	if err != nil {
		return nil, err
	}

	i, err := streams.Serialize(followers)
	if err != nil {
		return nil, err
	}

	return json.Marshal(i)
}

// dereferenceLocalUser is a shortcut to dereference followers an account on
// this instance, without making any external api/http calls.
//
// It is passed to new transports, and should only be invoked when the iri.Host == this host.
func (c *controller) dereferenceLocalUser(ctx context.Context, iri *url.URL) ([]byte, error) {
	user, err := c.fedDB.Get(ctx, iri)
	if err != nil {
		return nil, err
	}

	i, err := streams.Serialize(user)
	if err != nil {
		return nil, err
	}

	return json.Marshal(i)
}

// privkeyToPublicStr will create a string representation of RSA public key from private.
func privkeyToPublicStr(privkey *rsa.PrivateKey) string {
	b := x509.MarshalPKCS1PublicKey(&privkey.PublicKey)
	return byteutil.B2S(b)
}
