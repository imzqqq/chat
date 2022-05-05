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

package webfinger_test

import (
	"context"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/suite"
	"github.com/superseriousbusiness/gotosocial/internal/api/s2s/webfinger"
	"github.com/superseriousbusiness/gotosocial/internal/processing"
	"github.com/superseriousbusiness/gotosocial/testrig"
)

type WebfingerGetTestSuite struct {
	WebfingerStandardTestSuite
}

func (suite *WebfingerGetTestSuite) TestFingerUser() {
	targetAccount := suite.testAccounts["local_account_1"]

	// setup request
	requestPath := fmt.Sprintf("/%s?resource=acct:%s@%s", webfinger.WebfingerBasePath, targetAccount.Username, suite.config.Host)

	recorder := httptest.NewRecorder()
	ctx, _ := gin.CreateTestContext(recorder)
	ctx.Request = httptest.NewRequest(http.MethodGet, requestPath, nil) // the endpoint we're hitting

	// trigger the function being tested
	suite.webfingerModule.WebfingerGETRequest(ctx)

	// check response
	suite.EqualValues(http.StatusOK, recorder.Code)

	result := recorder.Result()
	defer result.Body.Close()
	b, err := ioutil.ReadAll(result.Body)
	assert.NoError(suite.T(), err)

	suite.Equal(`{"subject":"acct:the_mighty_zork@localhost:8080","aliases":["http://localhost:8080/users/the_mighty_zork","http://localhost:8080/@the_mighty_zork"],"links":[{"rel":"http://webfinger.net/rel/profile-page","type":"text/html","href":"http://localhost:8080/@the_mighty_zork"},{"rel":"self","type":"application/activity+json","href":"http://localhost:8080/users/the_mighty_zork"}]}`, string(b))
}

func (suite *WebfingerGetTestSuite) TestFingerUserWithDifferentAccountDomainByHost() {
	suite.config.Host = "gts.example.org"
	suite.config.AccountDomain = "example.org"
	suite.processor = processing.NewProcessor(suite.config, suite.tc, suite.federator, testrig.NewTestOauthServer(suite.db), testrig.NewTestMediaHandler(suite.db, suite.storage), suite.storage, testrig.NewTestTimelineManager(suite.db), suite.db, suite.emailSender)
	suite.webfingerModule = webfinger.New(suite.config, suite.processor).(*webfinger.Module)

	targetAccount := accountDomainAccount()
	if err := suite.db.Put(context.Background(), targetAccount); err != nil {
		panic(err)
	}

	// setup request
	requestPath := fmt.Sprintf("/%s?resource=acct:%s@%s", webfinger.WebfingerBasePath, targetAccount.Username, suite.config.Host)

	recorder := httptest.NewRecorder()
	ctx, _ := gin.CreateTestContext(recorder)
	ctx.Request = httptest.NewRequest(http.MethodGet, requestPath, nil) // the endpoint we're hitting

	// trigger the function being tested
	suite.webfingerModule.WebfingerGETRequest(ctx)

	// check response
	suite.EqualValues(http.StatusOK, recorder.Code)

	result := recorder.Result()
	defer result.Body.Close()
	b, err := ioutil.ReadAll(result.Body)
	assert.NoError(suite.T(), err)

	suite.Equal(`{"subject":"acct:aaaaa@example.org","aliases":["http://gts.example.org/users/aaaaa","http://gts.example.org/@aaaaa"],"links":[{"rel":"http://webfinger.net/rel/profile-page","type":"text/html","href":"http://gts.example.org/@aaaaa"},{"rel":"self","type":"application/activity+json","href":"http://gts.example.org/users/aaaaa"}]}`, string(b))
}

func (suite *WebfingerGetTestSuite) TestFingerUserWithDifferentAccountDomainByAccountDomain() {
	suite.config.Host = "gts.example.org"
	suite.config.AccountDomain = "example.org"
	suite.processor = processing.NewProcessor(suite.config, suite.tc, suite.federator, testrig.NewTestOauthServer(suite.db), testrig.NewTestMediaHandler(suite.db, suite.storage), suite.storage, testrig.NewTestTimelineManager(suite.db), suite.db, suite.emailSender)
	suite.webfingerModule = webfinger.New(suite.config, suite.processor).(*webfinger.Module)

	targetAccount := accountDomainAccount()
	if err := suite.db.Put(context.Background(), targetAccount); err != nil {
		panic(err)
	}

	// setup request
	requestPath := fmt.Sprintf("/%s?resource=acct:%s@%s", webfinger.WebfingerBasePath, targetAccount.Username, suite.config.AccountDomain)

	recorder := httptest.NewRecorder()
	ctx, _ := gin.CreateTestContext(recorder)
	ctx.Request = httptest.NewRequest(http.MethodGet, requestPath, nil) // the endpoint we're hitting

	// trigger the function being tested
	suite.webfingerModule.WebfingerGETRequest(ctx)

	// check response
	suite.EqualValues(http.StatusOK, recorder.Code)

	result := recorder.Result()
	defer result.Body.Close()
	b, err := ioutil.ReadAll(result.Body)
	assert.NoError(suite.T(), err)

	suite.Equal(`{"subject":"acct:aaaaa@example.org","aliases":["http://gts.example.org/users/aaaaa","http://gts.example.org/@aaaaa"],"links":[{"rel":"http://webfinger.net/rel/profile-page","type":"text/html","href":"http://gts.example.org/@aaaaa"},{"rel":"self","type":"application/activity+json","href":"http://gts.example.org/users/aaaaa"}]}`, string(b))
}

func (suite *WebfingerGetTestSuite) TestFingerUserWithoutAcct() {
	targetAccount := suite.testAccounts["local_account_1"]

	// setup request -- leave out the 'acct:' prefix, which is prettymuch what pixelfed currently does
	requestPath := fmt.Sprintf("/%s?resource=%s@%s", webfinger.WebfingerBasePath, targetAccount.Username, suite.config.Host)

	recorder := httptest.NewRecorder()
	ctx, _ := gin.CreateTestContext(recorder)
	ctx.Request = httptest.NewRequest(http.MethodGet, requestPath, nil) // the endpoint we're hitting

	// trigger the function being tested
	suite.webfingerModule.WebfingerGETRequest(ctx)

	// check response
	suite.EqualValues(http.StatusOK, recorder.Code)

	result := recorder.Result()
	defer result.Body.Close()
	b, err := ioutil.ReadAll(result.Body)
	assert.NoError(suite.T(), err)

	suite.Equal(`{"subject":"acct:the_mighty_zork@localhost:8080","aliases":["http://localhost:8080/users/the_mighty_zork","http://localhost:8080/@the_mighty_zork"],"links":[{"rel":"http://webfinger.net/rel/profile-page","type":"text/html","href":"http://localhost:8080/@the_mighty_zork"},{"rel":"self","type":"application/activity+json","href":"http://localhost:8080/users/the_mighty_zork"}]}`, string(b))
}

func TestWebfingerGetTestSuite(t *testing.T) {
	suite.Run(t, new(WebfingerGetTestSuite))
}
