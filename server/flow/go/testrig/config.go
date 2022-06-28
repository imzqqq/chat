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

package testrig

import (
	"os"
	"path"

	"github.com/coreos/go-oidc/v3/oidc"
	"github.com/superseriousbusiness/gotosocial/internal/config"
)

// InitTestConfig initializes viper configuration with test defaults.
func InitTestConfig() {
	config.Config(func(cfg *config.Configuration) {
		*cfg = TestDefaults
	})
}

// TestDefaults returns a Values struct with values set that are suitable for local testing.
var TestDefaults = config.Configuration{
	LogLevel:        "trace",
	LogDbQueries:    true,
	ApplicationName: "gotosocial",
	ConfigPath:      "",
	Host:            "localhost:8080",
	AccountDomain:   "localhost:8080",
	Protocol:        "http",
	BindAddress:     "127.0.0.1",
	Port:            8080,
	TrustedProxies:  []string{"127.0.0.1/32"},

	DbType:     "sqlite",
	DbAddress:  ":memory:",
	DbPort:     5432,
	DbUser:     "postgres",
	DbPassword: "postgres",
	DbDatabase: "postgres",

	WebTemplateBaseDir: "./web/template/",
	WebAssetBaseDir:    "./web/assets/",

	InstanceExposePeers:     true,
	InstanceExposeSuspended: true,

	AccountsRegistrationOpen: true,
	AccountsApprovalRequired: true,
	AccountsReasonRequired:   true,

	MediaImageMaxSize:        1048576, // 1mb
	MediaVideoMaxSize:        5242880, // 5mb
	MediaDescriptionMinChars: 0,
	MediaDescriptionMaxChars: 500,
	MediaRemoteCacheDays:     30,

	StorageBackend:       "local",
	StorageLocalBasePath: path.Join(os.TempDir(), "gotosocial"),

	StatusesMaxChars:           5000,
	StatusesCWMaxChars:         100,
	StatusesPollMaxOptions:     6,
	StatusesPollOptionMaxChars: 50,
	StatusesMediaMaxFiles:      6,

	LetsEncryptEnabled:      false,
	LetsEncryptPort:         0,
	LetsEncryptCertDir:      "",
	LetsEncryptEmailAddress: "",

	OIDCEnabled:          false,
	OIDCIdpName:          "",
	OIDCSkipVerification: false,
	OIDCIssuer:           "",
	OIDCClientID:         "",
	OIDCClientSecret:     "",
	OIDCScopes:           []string{oidc.ScopeOpenID, "profile", "email", "groups"},

	SMTPHost:     "",
	SMTPPort:     0,
	SMTPUsername: "",
	SMTPPassword: "",
	SMTPFrom:     "GoToSocial",

	SyslogEnabled:  false,
	SyslogProtocol: "udp",
	SyslogAddress:  "localhost:514",

	AdvancedCookiesSamesite: "lax",
}
