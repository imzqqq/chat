package com.imzqqq.app.ui

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.imzqqq.app.R
import com.imzqqq.app.espresso.tools.ScreenshotFailureRule
import com.imzqqq.app.features.MainActivity
import com.imzqqq.app.getString
import com.imzqqq.app.ui.robot.ElementRobot
import com.imzqqq.app.ui.robot.withDeveloperMode
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import java.util.UUID

/**
 * This test aim to open every possible screen of the application
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class UiAllScreensSanityTest {

    @get:Rule
    val testRule = RuleChain
            .outerRule(ActivityScenarioRule(MainActivity::class.java))
            .around(ScreenshotFailureRule())

    private val elementRobot = ElementRobot()

    // Last passing:
    // 2020-11-09
    // 2020-12-16 After ViewBinding huge change
    // 2021-04-08 Testing 429 change
    @Test
    fun allScreensTest() {
        // Create an account
        val userId = "UiTest_" + UUID.randomUUID().toString()
        elementRobot.signUp(userId)

        elementRobot.settings {
            general { crawl() }
            notifications { crawl() }
            preferences { crawl() }
            voiceAndVideo()
            ignoredUsers()
            securityAndPrivacy { crawl() }
            labs()
            advancedSettings { crawl() }
            helpAndAbout { crawl() }
        }

        elementRobot.newDirectMessage {
            verifyQrCodeButton()
            verifyInviteFriendsButton()
        }

        elementRobot.newRoom {
            createNewRoom {
                crawl()
                createRoom {
                    val message = "Hello world!"
                    postMessage(message)
                    crawl()
                    crawlMessage(message)
                    openSettings { crawl() }
                }
            }
        }

        elementRobot.withDeveloperMode {
            settings {
                advancedSettings { crawlDeveloperOptions() }
            }
            roomList {
                openRoom(getString(R.string.room_displayname_empty_room)) {
                    val message = "Test view source"
                    postMessage(message)
                    openMessageMenu(message) {
                        viewSource()
                    }
                }
            }
        }

        elementRobot.roomList {
            verifyCreatedRoom()
        }

        elementRobot.signout(expectSignOutWarning = true)

        // Login again on the same account
        elementRobot.login(userId)
        elementRobot.dismissVerificationIfPresent()
        // TODO Deactivate account instead of logout?
        elementRobot.signout(expectSignOutWarning = false)
    }
}
