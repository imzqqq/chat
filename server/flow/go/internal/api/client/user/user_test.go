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

package user_test

import (
	"codeberg.org/gruf/go-store/kv"
	"github.com/stretchr/testify/suite"
	"github.com/superseriousbusiness/gotosocial/internal/api/client/user"
	"github.com/superseriousbusiness/gotosocial/internal/config"
	"github.com/superseriousbusiness/gotosocial/internal/db"
	"github.com/superseriousbusiness/gotosocial/internal/email"
	"github.com/superseriousbusiness/gotosocial/internal/federation"
	"github.com/superseriousbusiness/gotosocial/internal/gtsmodel"
	"github.com/superseriousbusiness/gotosocial/internal/processing"
	"github.com/superseriousbusiness/gotosocial/internal/typeutils"
	"github.com/superseriousbusiness/gotosocial/testrig"
)

type UserStandardTestSuite struct {
	suite.Suite
	config      *config.Config
	db          db.DB
	tc          typeutils.TypeConverter
	federator   federation.Federator
	emailSender email.Sender
	processor   processing.Processor
	storage     *kv.KVStore

	testTokens       map[string]*gtsmodel.Token
	testClients      map[string]*gtsmodel.Client
	testApplications map[string]*gtsmodel.Application
	testUsers        map[string]*gtsmodel.User
	testAccounts     map[string]*gtsmodel.Account

	sentEmails map[string]string

	userModule *user.Module
}

func (suite *UserStandardTestSuite) SetupTest() {
	suite.testTokens = testrig.NewTestTokens()
	suite.testClients = testrig.NewTestClients()
	suite.testApplications = testrig.NewTestApplications()
	suite.testUsers = testrig.NewTestUsers()
	suite.testAccounts = testrig.NewTestAccounts()
	suite.config = testrig.NewTestConfig()
	suite.db = testrig.NewTestDB()
	suite.storage = testrig.NewTestStorage()
	testrig.InitTestLog()
	suite.tc = testrig.NewTestTypeConverter(suite.db)
	suite.federator = testrig.NewTestFederator(suite.db, testrig.NewTestTransportController(testrig.NewMockHTTPClient(nil), suite.db), suite.storage)
	suite.sentEmails = make(map[string]string)
	suite.emailSender = testrig.NewEmailSender("../../../../web/template/", suite.sentEmails)
	suite.processor = testrig.NewTestProcessor(suite.db, suite.storage, suite.federator, suite.emailSender)
	suite.userModule = user.New(suite.config, suite.processor).(*user.Module)
	testrig.StandardDBSetup(suite.db, suite.testAccounts)
	testrig.StandardStorageSetup(suite.storage, "../../../../testrig/media")
}

func (suite *UserStandardTestSuite) TearDownTest() {
	testrig.StandardDBTeardown(suite.db)
	testrig.StandardStorageTeardown(suite.storage)
}
