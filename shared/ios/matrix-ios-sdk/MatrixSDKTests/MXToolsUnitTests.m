/*
 Copyright 2014 OpenMarket Ltd

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

#import <XCTest/XCTest.h>

#import "MXTools.h"

@interface MXToolsUnitTests : XCTestCase

@end

@implementation MXToolsUnitTests

- (void)setUp
{
    [super setUp];
}

- (void)tearDown
{
    [super tearDown];
}

- (void)testGenerateSecret
{
    NSString *secret = [MXTools generateSecret];

    XCTAssertNotNil(secret);
}

- (void)testMatrixIdentifiers
{
    // Tests on homeserver domain (https://chat.imzqqq.top/docs/spec/legacy/#users)
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:chat.imzqqq.top"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:chat1234.chat.imzqqq.top"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:chat-1234.chat.imzqqq.top"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:chat-1234.aa.bbbb.chat.imzqqq.top"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:chat.imzqqq.top:8480"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:localhost"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:localhost:8480"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:127.0.0.1"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob:127.0.0.1:8480"]);
    
    XCTAssertFalse([MXTools isMatrixUserIdentifier:@"@bob:matrix+25.org"]);
    XCTAssertFalse([MXTools isMatrixUserIdentifier:@"@bob:matrix[].org"]);
    XCTAssertFalse([MXTools isMatrixUserIdentifier:@"@bob:chat.imzqqq.top."]);
    XCTAssertFalse([MXTools isMatrixUserIdentifier:@"@bob:chat.imzqqq.top-"]);
    XCTAssertFalse([MXTools isMatrixUserIdentifier:@"@bob:matrix-.org"]);
    XCTAssertFalse([MXTools isMatrixUserIdentifier:@"@bob:matrix.&aaz.org"]);
    
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@Bob:chat.imzqqq.top"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@bob1234:chat.imzqqq.top"]);
    XCTAssertTrue([MXTools isMatrixUserIdentifier:@"@+33012:chat.imzqqq.top"]);

    XCTAssertTrue([MXTools isMatrixEventIdentifier:@"$123456EventId:chat.imzqqq.top"]);
    XCTAssertTrue([MXTools isMatrixEventIdentifier:@"$pmOSN/DognfuSfhdW/qivXT19lfCWpdSfaPFKDBTJUk+"]);

    XCTAssertTrue([MXTools isMatrixRoomIdentifier:@"!an1234Room:chat.imzqqq.top"]);

    XCTAssertTrue([MXTools isMatrixRoomAlias:@"#matrix:chat.imzqqq.top"]);
    XCTAssertTrue([MXTools isMatrixRoomAlias:@"#matrix:chat.imzqqq.top:1234"]);

    XCTAssertTrue([MXTools isMatrixGroupIdentifier:@"+matrix:chat.imzqqq.top"]);
}


#pragma mark - Strings encoding

// Matrix identifiers can be found at https://chat.imzqqq.top/docs/spec/appendices.html#common-identifier-format
- (void)testRoomIdEscaping
{
    NSString *string = @"!tDRGDwZwQnlkowsjsm:chat.imzqqq.top";
    XCTAssertEqualObjects([MXTools encodeURIComponent:string], @"!tDRGDwZwQnlkowsjsm%3Amatrix.org");
}

- (void)testRoomAliasEscaping
{
    NSString *string = @"#riot-ios:chat.imzqqq.top";
    XCTAssertEqualObjects([MXTools encodeURIComponent:string], @"%23riot-ios%3Amatrix.org");
}

- (void)testEventIdEscaping
{
    NSString *string = @"$155006612045UiBxj:chat.imzqqq.top";
    XCTAssertEqualObjects([MXTools encodeURIComponent:string], @"%24155006612045UiBxj%3Amatrix.org");
}

- (void)testV3EventIdEscaping
{
    NSString *string = @"$pmOSN/DognfuSfhdW/qivXT19lfCWpdSfaPFKDBTJUk+";
    XCTAssertEqualObjects([MXTools encodeURIComponent:string], @"%24pmOSN%2FDognfuSfhdW%2FqivXT19lfCWpdSfaPFKDBTJUk%2B");
}

- (void)testGroupIdEscaping
{
    NSString *string = @"+matrix:chat.imzqqq.top";
    XCTAssertEqualObjects([MXTools encodeURIComponent:string], @"%2Bmatrix%3Amatrix.org");
}


#pragma mark - File extensions

- (void)testFileExtensionFromImageJPEGContentType
{
    XCTAssertEqualObjects([MXTools fileExtensionFromContentType:@"image/jpeg"], @".jpeg");
}

@end
