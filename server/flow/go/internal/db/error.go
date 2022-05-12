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

import "fmt"

// Error denotes a database error.
type Error error

var (
	// ErrNoEntries is returned when a caller expected an entry for a query, but none was found.
	ErrNoEntries Error = fmt.Errorf("no entries")
	// ErrMultipleEntries is returned when a caller expected ONE entry for a query, but multiples were found.
	ErrMultipleEntries Error = fmt.Errorf("multiple entries")
	// ErrUnknown denotes an unknown database error.
	ErrUnknown Error = fmt.Errorf("unknown error")
)

// ErrAlreadyExists is returned when a caller tries to insert a database entry that already exists in the db.
type ErrAlreadyExists struct {
	message string
}

func (e *ErrAlreadyExists) Error() string {
	return e.message
}

func NewErrAlreadyExists(msg string) error {
	return &ErrAlreadyExists{message: msg}
}
