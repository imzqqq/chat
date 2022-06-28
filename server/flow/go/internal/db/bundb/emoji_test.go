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

package bundb_test

import (
	"context"
	"testing"

	"github.com/stretchr/testify/suite"
)

type EmojiTestSuite struct {
	BunDBStandardTestSuite
}

func (suite *EmojiTestSuite) TestGetCustomEmojis() {
	emojis, err := suite.db.GetCustomEmojis(context.Background())

	suite.NoError(err)
	suite.Equal(1, len(emojis))
	suite.Equal("rainbow", emojis[0].Shortcode)
}

func TestEmojiTestSuite(t *testing.T) {
	suite.Run(t, new(EmojiTestSuite))
}
