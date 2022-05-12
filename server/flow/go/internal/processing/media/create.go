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

package media

import (
	"context"
	"fmt"
	"io"

	apimodel "github.com/superseriousbusiness/gotosocial/internal/api/model"
	"github.com/superseriousbusiness/gotosocial/internal/gtsmodel"
	"github.com/superseriousbusiness/gotosocial/internal/media"
)

func (p *processor) Create(ctx context.Context, account *gtsmodel.Account, form *apimodel.AttachmentRequest) (*apimodel.Attachment, error) {
	data := func(innerCtx context.Context) (io.Reader, int, error) {
		f, err := form.File.Open()
		return f, int(form.File.Size), err
	}

	focusX, focusY, err := parseFocus(form.Focus)
	if err != nil {
		return nil, fmt.Errorf("could not parse focus value %s: %s", form.Focus, err)
	}

	// process the media attachment and load it immediately
	media, err := p.mediaManager.ProcessMedia(ctx, data, nil, account.ID, &media.AdditionalMediaInfo{
		Description: &form.Description,
		FocusX:      &focusX,
		FocusY:      &focusY,
	})
	if err != nil {
		return nil, err
	}

	attachment, err := media.LoadAttachment(ctx)
	if err != nil {
		return nil, err
	}

	// prepare the frontend representation now -- if there are any errors here at least we can bail without
	// having already put something in the database and then having to clean it up again (eugh)
	apiAttachment, err := p.tc.AttachmentToAPIAttachment(ctx, attachment)
	if err != nil {
		return nil, fmt.Errorf("error parsing media attachment to frontend type: %s", err)
	}

	return &apiAttachment, nil
}
