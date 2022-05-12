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

package trans

import (
	"context"

	"github.com/superseriousbusiness/gotosocial/internal/db"
)

// Importer wraps functionality for importing entries from a file into the database.
type Importer interface {
	Import(ctx context.Context, path string) error
}

type importer struct {
	db db.DB
}

// NewImporter returns a new Importer interface that uses the given db.
func NewImporter(db db.DB) Importer {
	return &importer{
		db: db,
	}
}
