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

package webfinger

import (
	"net/http"

	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/processing"
	"github.com/superseriousbusiness/gotosocial/internal/router"
)

const (
	// WebfingerBasePath is the base path for serving webfinger lookup requests
	WebfingerBasePath = ".well-known/webfinger"
)

// Module implements the FederationModule interface
type Module struct {
	processor processing.Processor
}

// New returns a new webfinger module
func New(processor processing.Processor) api.FederationModule {
	return &Module{
		processor: processor,
	}
}

// Route satisfies the FederationModule interface
func (m *Module) Route(s router.Router) error {
	s.AttachHandler(http.MethodGet, WebfingerBasePath, m.WebfingerGETRequest)
	return nil
}
