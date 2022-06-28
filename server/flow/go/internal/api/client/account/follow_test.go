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

package account_test

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/suite"
	"github.com/superseriousbusiness/gotosocial/internal/api/client/account"
	"github.com/superseriousbusiness/gotosocial/internal/oauth"
)

type FollowTestSuite struct {
	AccountStandardTestSuite
}

func (suite *FollowTestSuite) TestFollowSelf() {
	testAcct := suite.testAccounts["local_account_1"]
	recorder := httptest.NewRecorder()
	ctx, _ := gin.CreateTestContext(recorder)
	ctx.Set(oauth.SessionAuthorizedAccount, testAcct)
	ctx.Set(oauth.SessionAuthorizedToken, oauth.DBTokenToToken(suite.testTokens["local_account_1"]))
	ctx.Set(oauth.SessionAuthorizedApplication, suite.testApplications["application_1"])
	ctx.Set(oauth.SessionAuthorizedUser, suite.testUsers["local_account_1"])
	ctx.Request = httptest.NewRequest(http.MethodPost, fmt.Sprintf("http://localhost:8080%s", strings.Replace(account.FollowPath, ":id", testAcct.ID, 1)), nil)

	ctx.Params = gin.Params{
		gin.Param{
			Key:   account.IDKey,
			Value: testAcct.ID,
		},
	}

	// call the handler
	suite.accountModule.AccountFollowPOSTHandler(ctx)

	// 1. status should be Not Acceptable due to self-follow attempt
	suite.Equal(http.StatusNotAcceptable, recorder.Code)

	result := recorder.Result()
	defer result.Body.Close()

	// check the response
	b, err := ioutil.ReadAll(result.Body)
	_ = b
	assert.NoError(suite.T(), err)
}

func TestFollowTestSuite(t *testing.T) {
	suite.Run(t, new(FollowTestSuite))
}
