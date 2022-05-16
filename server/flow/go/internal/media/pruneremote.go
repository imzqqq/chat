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
	"time"

	"codeberg.org/gruf/go-store/storage"
	"github.com/sirupsen/logrus"
	"github.com/superseriousbusiness/gotosocial/internal/db"
	"github.com/superseriousbusiness/gotosocial/internal/gtsmodel"
)

func (m *manager) PruneAllRemote(ctx context.Context, olderThanDays int) (int, error) {
	var totalPruned int

	// convert days into a duration string
	olderThanHoursString := fmt.Sprintf("%dh", olderThanDays*24)
	// parse the duration string into a duration
	olderThanHours, err := time.ParseDuration(olderThanHoursString)
	if err != nil {
		return totalPruned, fmt.Errorf("PruneAllRemote: %d", err)
	}
	// 'subtract' that from the time now to give our threshold
	olderThan := time.Now().Add(-olderThanHours)
	logrus.Infof("PruneAllRemote: pruning media older than %s", olderThan)

	// select 20 attachments at a time and prune them
	for attachments, err := m.db.GetRemoteOlderThan(ctx, olderThan, selectPruneLimit); err == nil && len(attachments) != 0; attachments, err = m.db.GetRemoteOlderThan(ctx, olderThan, selectPruneLimit) {

		// use the age of the oldest attachment (the last one in the slice) as the next 'older than' value
		l := len(attachments)
		logrus.Tracef("PruneAllRemote: got %d attachments older than %s", l, olderThan)
		olderThan = attachments[l-1].CreatedAt

		// prune each attachment
		for _, attachment := range attachments {
			if err := m.pruneOneRemote(ctx, attachment); err != nil {
				return totalPruned, err
			}
			totalPruned++
		}
	}

	// make sure we don't have a real error when we leave the loop
	if err != nil && err != db.ErrNoEntries {
		return totalPruned, err
	}

	logrus.Infof("PruneAllRemote: finished pruning remote media: pruned %d entries", totalPruned)
	return totalPruned, nil
}

func (m *manager) pruneOneRemote(ctx context.Context, attachment *gtsmodel.MediaAttachment) error {
	if attachment.File.Path != "" {
		// delete the full size attachment from storage
		logrus.Tracef("pruneOneRemote: deleting %s", attachment.File.Path)
		if err := m.storage.Delete(attachment.File.Path); err != nil && err != storage.ErrNotFound {
			return err
		}
		attachment.Cached = false
	}

	if attachment.Thumbnail.Path != "" {
		// delete the thumbnail from storage
		logrus.Tracef("pruneOneRemote: deleting %s", attachment.Thumbnail.Path)
		if err := m.storage.Delete(attachment.Thumbnail.Path); err != nil && err != storage.ErrNotFound {
			return err
		}
		attachment.Cached = false
	}

	// update the attachment to reflect that we no longer have it cached
	return m.db.UpdateByPrimaryKey(ctx, attachment)
}
