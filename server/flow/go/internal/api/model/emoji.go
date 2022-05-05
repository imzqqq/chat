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

package model

import "mime/multipart"

// Emoji represents a custom emoji.
//
// swagger:model emoji
type Emoji struct {
	// The name of the custom emoji.
	// example: blobcat_uwu
	Shortcode string `json:"shortcode"`
	// Web URL of the custom emoji.
	// example: https://example.org/fileserver/emojis/blogcat_uwu.gif
	URL string `json:"url"`
	// A link to a static copy of the custom emoji.
	// example: https://example.org/fileserver/emojis/blogcat_uwu.png
	StaticURL string `json:"static_url"`
	// Emoji is visible in the emoji picker of the instance.
	// example: true
	VisibleInPicker bool `json:"visible_in_picker"`
	// Used for sorting custom emoji in the picker.
	// example: blobcats
	Category string `json:"category,omitempty"`
}

// EmojiCreateRequest represents a request to create a custom emoji made through the admin API.
//
// swagger:model emojiCreateRequest
type EmojiCreateRequest struct {
	// Desired shortcode for the emoji, without surrounding colons. This must be unique for the domain.
	// example: blobcat_uwu
	Shortcode string `form:"shortcode" validation:"required"`
	// Image file to use for the emoji. Must be png or gif and no larger than 50kb.
	Image *multipart.FileHeader `form:"image" validation:"required"`
}
