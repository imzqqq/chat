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

package db

// Where allows the caller of the DB to specify Where parameters.
type Where struct {
	// The table to search on.
	Key string
	// The value to match.
	Value interface{}
	// Whether the value (if a string) should be case sensitive or not.
	// Defaults to false.
	CaseInsensitive bool
	// If set, reverse the where.
	// `WHERE k = v` becomes `WHERE k != v`.
	// `WHERE k IS NULL` becomes `WHERE k IS NOT NULL`
	Not bool
}
