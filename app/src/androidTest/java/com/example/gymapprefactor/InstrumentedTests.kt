package com.example.gymapprefactor

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.gymapprefactor.app.MainActivity
import com.example.gymapprefactor.app.util.dispatcher.DispatcherModule
import com.example.gymapprefactor.business.network.UserStorage
import com.example.gymapprefactor.testutil.TestEnvironment
import com.example.gymapprefactor.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DispatcherModule::class)
class InstrumentedTests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject lateinit var userStorage: UserStorage

    @Before
    fun setup() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.get())
    }

    @After
    fun tearDown() {
        TestEnvironment.setNoUserEnvironment()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.get())
    }


    @Test
    fun verifyUserLoadedFromStorage() {
        TestEnvironment.setUserPresentEnvironment()

        activityRule.scenario.onActivity {
            val user = runBlocking { userStorage.loadUser() }
            Assert.assertEquals("PreSavedUser", user?.username)
            Assert.assertEquals(777, user?.runesCount)
        }
    }

    @Test
    fun verifyUserCreatedWithEmptyStorage() {
        runBlocking {
            delay(5000)
        }

        activityRule.scenario.onActivity { activity ->
            val model = activity.appDataModel
            val user = model.getCurrentUser()

            Assert.assertNotNull("User should have been created and not be null", user)
            Assert.assertEquals("Username", user.username)
            Assert.assertEquals(100, user.runesCount)
        }
    }
}
