import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.triplogger.R
import com.example.triplogger.view.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)


    @After
    fun tearDown() {
        // Close activity scenario if needed
        activityScenarioRule.scenario.close()
    }

    @Test
    fun testLoginWithValidCredentials() {
        // Dismiss language selection dialog if needed
        onView(withText("English")).perform(click())

        // Enter login credentials
        onView(withId(R.id.username))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.password))
            .perform(typeText("password123"), closeSoftKeyboard())

        // Click login button
        onView(withId(R.id.login_button)).perform(click())

        // Verify that MainActivity is displayed
        onView(withId(R.id.fragment_map_container)).check(matches(isDisplayed()))
    }

    @Test
    fun testLanguageSelectionDialogAppears() {
        // Check if the language selection dialog is displayed
        onView(withText(R.string.select_language)).check(matches(isDisplayed()))

        // Select English (assuming it's the first option)
        onView(withText("English")).perform(click())

        // Check if the login screen is displayed after selecting a language
        onView(withId(R.id.username)).check(matches(isDisplayed()))
    }

}
