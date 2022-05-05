package db

import (
	"context"

	"github.com/superseriousbusiness/gotosocial/internal/gtsmodel"
)

const (
	// DBTypePostgres represents an underlying POSTGRES database type.
	DBTypePostgres string = "POSTGRES"
)

// DB provides methods for interacting with an underlying database or other storage mechanism.
type DB interface {
	Account
	Admin
	Basic
	Domain
	Instance
	Media
	Mention
	Notification
	Relationship
	Session
	Status
	Timeline

	/*
		USEFUL CONVERSION FUNCTIONS
	*/

	// MentionStringsToMentions takes a slice of deduplicated, lowercase account names in the form "@test@whatever.example.org" for a remote account,
	// or @test for a local account, which have been mentioned in a status.
	// It takes the id of the account that wrote the status, and the id of the status itself, and then
	// checks in the database for the mentioned accounts, and returns a slice of mentions generated based on the given parameters.
	//
	// Note: this func doesn't/shouldn't do any manipulation of the accounts in the DB, it's just for checking
	// if they exist in the db and conveniently returning them if they do.
	MentionStringsToMentions(ctx context.Context, targetAccounts []string, originAccountID string, statusID string) ([]*gtsmodel.Mention, error)

	// TagStringsToTags takes a slice of deduplicated, lowercase tags in the form "somehashtag", which have been
	// used in a status. It takes the id of the account that wrote the status, and the id of the status itself, and then
	// returns a slice of *model.Tag corresponding to the given tags. If the tag already exists in database, that tag
	// will be returned. Otherwise a pointer to a new tag struct will be created and returned.
	//
	// Note: this func doesn't/shouldn't do any manipulation of the tags in the DB, it's just for checking
	// if they exist in the db already, and conveniently returning them, or creating new tag structs.
	TagStringsToTags(ctx context.Context, tags []string, originAccountID string) ([]*gtsmodel.Tag, error)

	// EmojiStringsToEmojis takes a slice of deduplicated, lowercase emojis in the form ":emojiname:", which have been
	// used in a status. It takes the id of the account that wrote the status, and the id of the status itself, and then
	// returns a slice of *model.Emoji corresponding to the given emojis.
	//
	// Note: this func doesn't/shouldn't do any manipulation of the emoji in the DB, it's just for checking
	// if they exist in the db and conveniently returning them if they do.
	EmojiStringsToEmojis(ctx context.Context, emojis []string) ([]*gtsmodel.Emoji, error)
}
