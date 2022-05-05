/*
   GoToSocial
   Copyright (C) 2021 GoToSocial Authors admin@gotosocial.org

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

package account

import (
	"bytes"
	"context"
	"errors"
	"fmt"
	"github.com/sirupsen/logrus"
	"io"
	"mime/multipart"

	"github.com/superseriousbusiness/gotosocial/internal/ap"
	apimodel "github.com/superseriousbusiness/gotosocial/internal/api/model"
	"github.com/superseriousbusiness/gotosocial/internal/gtsmodel"
	"github.com/superseriousbusiness/gotosocial/internal/media"
	"github.com/superseriousbusiness/gotosocial/internal/messages"
	"github.com/superseriousbusiness/gotosocial/internal/text"
	"github.com/superseriousbusiness/gotosocial/internal/util"
	"github.com/superseriousbusiness/gotosocial/internal/validate"
)

func (p *processor) Update(ctx context.Context, account *gtsmodel.Account, form *apimodel.UpdateCredentialsRequest) (*apimodel.Account, error) {
	l := logrus.WithField("func", "AccountUpdate")

	if form.Discoverable != nil {
		account.Discoverable = *form.Discoverable
	}

	if form.Bot != nil {
		account.Bot = *form.Bot
	}

	if form.DisplayName != nil {
		if err := validate.DisplayName(*form.DisplayName); err != nil {
			return nil, err
		}
		account.DisplayName = text.RemoveHTML(*form.DisplayName)
	}

	if form.Note != nil {
		if err := validate.Note(*form.Note); err != nil {
			return nil, err
		}
		note, err := p.processNote(ctx, *form.Note, account.ID)
		if err != nil {
			return nil, err
		}
		account.Note = note
	}

	if form.Avatar != nil && form.Avatar.Size != 0 {
		avatarInfo, err := p.UpdateAvatar(ctx, form.Avatar, account.ID)
		if err != nil {
			return nil, err
		}
		account.AvatarMediaAttachmentID = avatarInfo.ID
		account.AvatarMediaAttachment = avatarInfo
		l.Tracef("new avatar info for account %s is %+v", account.ID, avatarInfo)
	}

	if form.Header != nil && form.Header.Size != 0 {
		headerInfo, err := p.UpdateHeader(ctx, form.Header, account.ID)
		if err != nil {
			return nil, err
		}
		account.HeaderMediaAttachmentID = headerInfo.ID
		account.HeaderMediaAttachment = headerInfo
		l.Tracef("new header info for account %s is %+v", account.ID, headerInfo)
	}

	if form.Locked != nil {
		account.Locked = *form.Locked
	}

	if form.Source != nil {
		if form.Source.Language != nil {
			if err := validate.Language(*form.Source.Language); err != nil {
				return nil, err
			}
			account.Language = *form.Source.Language
		}

		if form.Source.Sensitive != nil {
			account.Sensitive = *form.Source.Sensitive
		}

		if form.Source.Privacy != nil {
			if err := validate.Privacy(*form.Source.Privacy); err != nil {
				return nil, err
			}
			privacy := p.tc.APIVisToVis(apimodel.Visibility(*form.Source.Privacy))
			account.Privacy = privacy
		}
	}

	updatedAccount, err := p.db.UpdateAccount(ctx, account)
	if err != nil {
		return nil, fmt.Errorf("could not update account %s: %s", account.ID, err)
	}

	p.fromClientAPI <- messages.FromClientAPI{
		APObjectType:   ap.ObjectProfile,
		APActivityType: ap.ActivityUpdate,
		GTSModel:       updatedAccount,
		OriginAccount:  updatedAccount,
	}

	acctSensitive, err := p.tc.AccountToAPIAccountSensitive(ctx, updatedAccount)
	if err != nil {
		return nil, fmt.Errorf("could not convert account into apisensitive account: %s", err)
	}
	return acctSensitive, nil
}

// UpdateAvatar does the dirty work of checking the avatar part of an account update form,
// parsing and checking the image, and doing the necessary updates in the database for this to become
// the account's new avatar image.
func (p *processor) UpdateAvatar(ctx context.Context, avatar *multipart.FileHeader, accountID string) (*gtsmodel.MediaAttachment, error) {
	var err error
	if int(avatar.Size) > p.config.MediaConfig.MaxImageSize {
		err = fmt.Errorf("avatar with size %d exceeded max image size of %d bytes", avatar.Size, p.config.MediaConfig.MaxImageSize)
		return nil, err
	}
	f, err := avatar.Open()
	if err != nil {
		return nil, fmt.Errorf("could not read provided avatar: %s", err)
	}

	// extract the bytes
	buf := new(bytes.Buffer)
	size, err := io.Copy(buf, f)
	if err != nil {
		return nil, fmt.Errorf("could not read provided avatar: %s", err)
	}
	if size == 0 {
		return nil, errors.New("could not read provided avatar: size 0 bytes")
	}

	// do the setting
	avatarInfo, err := p.mediaHandler.ProcessHeaderOrAvatar(ctx, buf.Bytes(), accountID, media.Avatar, "")
	if err != nil {
		return nil, fmt.Errorf("error processing avatar: %s", err)
	}

	return avatarInfo, f.Close()
}

// UpdateHeader does the dirty work of checking the header part of an account update form,
// parsing and checking the image, and doing the necessary updates in the database for this to become
// the account's new header image.
func (p *processor) UpdateHeader(ctx context.Context, header *multipart.FileHeader, accountID string) (*gtsmodel.MediaAttachment, error) {
	var err error
	if int(header.Size) > p.config.MediaConfig.MaxImageSize {
		err = fmt.Errorf("header with size %d exceeded max image size of %d bytes", header.Size, p.config.MediaConfig.MaxImageSize)
		return nil, err
	}
	f, err := header.Open()
	if err != nil {
		return nil, fmt.Errorf("could not read provided header: %s", err)
	}

	// extract the bytes
	buf := new(bytes.Buffer)
	size, err := io.Copy(buf, f)
	if err != nil {
		return nil, fmt.Errorf("could not read provided header: %s", err)
	}
	if size == 0 {
		return nil, errors.New("could not read provided header: size 0 bytes")
	}

	// do the setting
	headerInfo, err := p.mediaHandler.ProcessHeaderOrAvatar(ctx, buf.Bytes(), accountID, media.Header, "")
	if err != nil {
		return nil, fmt.Errorf("error processing header: %s", err)
	}

	return headerInfo, f.Close()
}

func (p *processor) processNote(ctx context.Context, note string, accountID string) (string, error) {
	if note == "" {
		return "", nil
	}

	tagStrings := util.DeriveHashtagsFromText(note)
	tags, err := p.db.TagStringsToTags(ctx, tagStrings, accountID)
	if err != nil {
		return "", err
	}

	mentionStrings := util.DeriveMentionsFromText(note)
	mentions, err := p.db.MentionStringsToMentions(ctx, mentionStrings, accountID, "")
	if err != nil {
		return "", err
	}

	// TODO: support emojis in account notes
	// emojiStrings := util.DeriveEmojisFromText(note)
	// emojis, err := p.db.EmojiStringsToEmojis(ctx, emojiStrings)

	return p.formatter.FromPlain(ctx, note, mentions, tags), nil
}
