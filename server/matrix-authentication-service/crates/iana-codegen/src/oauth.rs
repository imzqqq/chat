// Copyright 2022 The Matrix.org Foundation C.I.C.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

use serde::Deserialize;

use crate::{
    traits::{s, Section},
    EnumEntry,
};

#[allow(dead_code)]
#[derive(Debug, Deserialize)]
pub struct AccessTokenType {
    #[serde(rename = "Name")]
    name: String,
    #[serde(rename = "Additional Token Endpoint Response Parameters")]
    additional_parameters: String,
    #[serde(rename = "HTTP Authentication Scheme(s)")]
    http_schemes: String,
    #[serde(rename = "Change Controller")]
    change_controller: String,
    #[serde(rename = "Reference")]
    reference: String,
}

impl EnumEntry for AccessTokenType {
    const URL: &'static str = "https://www.iana.org/assignments/oauth-parameters/token-types.csv";
    const SECTIONS: &'static [Section] = &[s("OAuthAccessTokenType", "OAuth Access Token Type")];

    fn key(&self) -> Option<&'static str> {
        Some("OAuthAccessTokenType")
    }

    fn name(&self) -> &str {
        &self.name
    }
}

#[allow(dead_code)]
#[derive(Debug, Deserialize)]
pub struct AuthorizationEndpointResponseType {
    #[serde(rename = "Name")]
    name: String,
    #[serde(rename = "Change Controller")]
    change_controller: String,
    #[serde(rename = "Reference")]
    reference: String,
}

impl EnumEntry for AuthorizationEndpointResponseType {
    const URL: &'static str = "https://www.iana.org/assignments/oauth-parameters/endpoint.csv";
    const SECTIONS: &'static [Section] = &[s(
        "OAuthAuthorizationEndpointResponseType",
        "OAuth Authorization Endpoint Response Type",
    )];

    fn key(&self) -> Option<&'static str> {
        Some("OAuthAuthorizationEndpointResponseType")
    }

    fn name(&self) -> &str {
        &self.name
    }
}

#[allow(dead_code)]
#[derive(Debug, Deserialize)]
pub struct TokenEndpointAuthenticationMethod {
    #[serde(rename = "Token Endpoint Authentication Method Name")]
    name: String,
    #[serde(rename = "Change Controller")]
    change_controller: String,
    #[serde(rename = "Reference")]
    reference: String,
}

#[allow(dead_code)]
#[derive(Debug, Deserialize)]
pub struct TokenTypeHint {
    #[serde(rename = "Hint Value")]
    name: String,
    #[serde(rename = "Change Controller")]
    change_controller: String,
    #[serde(rename = "Reference")]
    reference: String,
}

impl EnumEntry for TokenTypeHint {
    const URL: &'static str =
        "https://www.iana.org/assignments/oauth-parameters/token-type-hint.csv";
    const SECTIONS: &'static [Section] = &[s("OAuthTokenTypeHint", "OAuth Token Type Hint")];

    fn key(&self) -> Option<&'static str> {
        Some("OAuthTokenTypeHint")
    }

    fn name(&self) -> &str {
        &self.name
    }
}

impl EnumEntry for TokenEndpointAuthenticationMethod {
    const URL: &'static str =
        "https://www.iana.org/assignments/oauth-parameters/token-endpoint-auth-method.csv";
    const SECTIONS: &'static [Section] = &[s(
        "OAuthClientAuthenticationMethod",
        "OAuth Token Endpoint Authentication Method",
    )];

    fn key(&self) -> Option<&'static str> {
        Some("OAuthClientAuthenticationMethod")
    }

    fn name(&self) -> &str {
        &self.name
    }
}

#[allow(dead_code)]
#[derive(Debug, Deserialize)]
pub struct PkceCodeChallengeMethod {
    #[serde(rename = "Code Challenge Method Parameter Name")]
    name: String,
    #[serde(rename = "Change Controller")]
    change_controller: String,
    #[serde(rename = "Reference")]
    reference: String,
}

impl EnumEntry for PkceCodeChallengeMethod {
    const URL: &'static str =
        "https://www.iana.org/assignments/oauth-parameters/pkce-code-challenge-method.csv";
    const SECTIONS: &'static [Section] =
        &[s("PkceCodeChallengeMethod", "PKCE Code Challenge Method")];

    fn key(&self) -> Option<&'static str> {
        Some("PkceCodeChallengeMethod")
    }

    fn name(&self) -> &str {
        &self.name
    }
}
