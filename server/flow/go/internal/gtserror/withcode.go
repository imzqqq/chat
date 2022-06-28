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

package gtserror

import (
	"errors"
	"net/http"
	"strings"
)

// WithCode wraps an internal error with an http code, and a 'safe' version of
// the error that can be served to clients without revealing internal business logic.
//
// A typical use of this error would be to first log the Original error, then return
// the Safe error and the StatusCode to an API caller.
type WithCode interface {
	// Error returns the original internal error for debugging within the GoToSocial logs.
	// This should *NEVER* be returned to a client as it may contain sensitive information.
	Error() string
	// Safe returns the API-safe version of the error for serialization towards a client.
	// There's not much point logging this internally because it won't contain much helpful information.
	Safe() string
	//  Code returns the status code for serving to a client.
	Code() int
}

type withCode struct {
	original error
	safe     error
	code     int
}

func (e withCode) Error() string {
	return e.original.Error()
}

func (e withCode) Safe() string {
	return e.safe.Error()
}

func (e withCode) Code() int {
	return e.code
}

// NewErrorBadRequest returns an ErrorWithCode 400 with the given original error and optional help text.
func NewErrorBadRequest(original error, helpText ...string) WithCode {
	safe := http.StatusText(http.StatusBadRequest)
	if helpText != nil {
		safe = safe + ": " + strings.Join(helpText, ": ")
	}
	return withCode{
		original: original,
		safe:     errors.New(safe),
		code:     http.StatusBadRequest,
	}
}

// NewErrorUnauthorized returns an ErrorWithCode 401 with the given original error and optional help text.
func NewErrorUnauthorized(original error, helpText ...string) WithCode {
	safe := http.StatusText(http.StatusUnauthorized)
	if helpText != nil {
		safe = safe + ": " + strings.Join(helpText, ": ")
	}
	return withCode{
		original: original,
		safe:     errors.New(safe),
		code:     http.StatusUnauthorized,
	}
}

// NewErrorForbidden returns an ErrorWithCode 403 with the given original error and optional help text.
func NewErrorForbidden(original error, helpText ...string) WithCode {
	safe := http.StatusText(http.StatusForbidden)
	if helpText != nil {
		safe = safe + ": " + strings.Join(helpText, ": ")
	}
	return withCode{
		original: original,
		safe:     errors.New(safe),
		code:     http.StatusForbidden,
	}
}

// NewErrorNotFound returns an ErrorWithCode 404 with the given original error and optional help text.
func NewErrorNotFound(original error, helpText ...string) WithCode {
	safe := http.StatusText(http.StatusNotFound)
	if helpText != nil {
		safe = safe + ": " + strings.Join(helpText, ": ")
	}
	return withCode{
		original: original,
		safe:     errors.New(safe),
		code:     http.StatusNotFound,
	}
}

// NewErrorInternalError returns an ErrorWithCode 500 with the given original error and optional help text.
func NewErrorInternalError(original error, helpText ...string) WithCode {
	safe := http.StatusText(http.StatusInternalServerError)
	if helpText != nil {
		safe = safe + ": " + strings.Join(helpText, ": ")
	}
	return withCode{
		original: original,
		safe:     errors.New(safe),
		code:     http.StatusInternalServerError,
	}
}

// NewErrorConflict returns an ErrorWithCode 409 with the given original error and optional help text.
func NewErrorConflict(original error, helpText ...string) WithCode {
	safe := http.StatusText(http.StatusConflict)
	if helpText != nil {
		safe = safe + ": " + strings.Join(helpText, ": ")
	}
	return withCode{
		original: original,
		safe:     errors.New(safe),
		code:     http.StatusConflict,
	}
}

// NewErrorNotAcceptable returns an ErrorWithCode 406 with the given original error and optional help text.
func NewErrorNotAcceptable(original error, helpText ...string) WithCode {
	safe := http.StatusText(http.StatusNotAcceptable)
	if helpText != nil {
		safe = safe + ": " + strings.Join(helpText, ": ")
	}
	return withCode{
		original: original,
		safe:     errors.New(safe),
		code:     http.StatusNotAcceptable,
	}
}

// NewErrorUnprocessableEntity returns an ErrorWithCode 422 with the given original error and optional help text.
func NewErrorUnprocessableEntity(original error, helpText ...string) WithCode {
	safe := http.StatusText(http.StatusUnprocessableEntity)
	if helpText != nil {
		safe = safe + ": " + strings.Join(helpText, ": ")
	}
	return withCode{
		original: original,
		safe:     errors.New(safe),
		code:     http.StatusUnprocessableEntity,
	}
}
