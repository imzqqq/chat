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

package status

import (
	"errors"
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/superseriousbusiness/gotosocial/internal/api"
	"github.com/superseriousbusiness/gotosocial/internal/api/model"
	"github.com/superseriousbusiness/gotosocial/internal/config"
	"github.com/superseriousbusiness/gotosocial/internal/gtserror"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
	"github.com/superseriousbusiness/gotosocial/internal/validate"
)

// StatusCreatePOSTHandler swagger:operation POST /api/v1/statuses statusCreate
//
// Create a new status.
//
// The parameters can also be given in the body of the request, as JSON, if the content-type is set to 'application/json'.
// The parameters can also be given in the body of the request, as XML, if the content-type is set to 'application/xml'.
//
// ---
// tags:
// - statuses
//
// consumes:
// - application/json
// - application/xml
// - application/x-www-form-urlencoded
//
// produces:
// - application/json
//
// security:
// - OAuth2 Bearer:
//   - write:statuses
//
// responses:
//   '200':
//     description: "The newly created status."
//     schema:
//       "$ref": "#/definitions/status"
//   '400':
//      description: bad request
//   '401':
//      description: unauthorized
//   '403':
//      description: forbidden
//   '404':
//      description: not found
//   '406':
//      description: not acceptable
//   '500':
//      description: internal server error
func (m *Module) StatusCreatePOSTHandler(c *gin.Context) {
	authed, err := oauth.Authed(c, true, true, true, true)
	if err != nil {
		api.ErrorHandler(c, gtserror.NewErrorUnauthorized(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if _, err := api.NegotiateAccept(c, api.JSONAcceptHeaders...); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorNotAcceptable(err, err.Error()), m.processor.InstanceGet)
		return
	}

	form := &model.AdvancedStatusCreateForm{}
	if err := c.ShouldBind(form); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	if err := validateCreateStatus(form); err != nil {
		api.ErrorHandler(c, gtserror.NewErrorBadRequest(err, err.Error()), m.processor.InstanceGet)
		return
	}

	apiStatus, errWithCode := m.processor.StatusCreate(c.Request.Context(), authed, form)
	if errWithCode != nil {
		api.ErrorHandler(c, errWithCode, m.processor.InstanceGet)
		return
	}

	c.JSON(http.StatusOK, apiStatus)
}

func validateCreateStatus(form *model.AdvancedStatusCreateForm) error {
	if form.Status == "" && form.MediaIDs == nil && form.Poll == nil {
		return errors.New("no status, media, or poll provided")
	}

	if form.MediaIDs != nil && form.Poll != nil {
		return errors.New("can't post media + poll in same status")
	}

	maxChars := config.GetStatusesMaxChars()
	maxMediaFiles := config.GetStatusesMediaMaxFiles()
	maxPollOptions := config.GetStatusesPollMaxOptions()
	maxPollChars := config.GetStatusesPollOptionMaxChars()
	maxCwChars := config.GetStatusesCWMaxChars()

	if form.Status != "" {
		if len(form.Status) > maxChars {
			return fmt.Errorf("status too long, %d characters provided but limit is %d", len(form.Status), maxChars)
		}
	}

	if len(form.MediaIDs) > maxMediaFiles {
		return fmt.Errorf("too many media files attached to status, %d attached but limit is %d", len(form.MediaIDs), maxMediaFiles)
	}

	if form.Poll != nil {
		if form.Poll.Options == nil {
			return errors.New("poll with no options")
		}
		if len(form.Poll.Options) > maxPollOptions {
			return fmt.Errorf("too many poll options provided, %d provided but limit is %d", len(form.Poll.Options), maxPollOptions)
		}
		for _, p := range form.Poll.Options {
			if len(p) > maxPollChars {
				return fmt.Errorf("poll option too long, %d characters provided but limit is %d", len(p), maxPollChars)
			}
		}
	}

	if form.SpoilerText != "" {
		if len(form.SpoilerText) > maxCwChars {
			return fmt.Errorf("content-warning/spoilertext too long, %d characters provided but limit is %d", len(form.SpoilerText), maxCwChars)
		}
	}

	if form.Language != "" {
		if err := validate.Language(form.Language); err != nil {
			return err
		}
	}

	return nil
}
