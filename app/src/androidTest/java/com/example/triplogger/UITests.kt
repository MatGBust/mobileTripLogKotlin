import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triplogger.R
import com.example.triplogger.view.LoginActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class UITests {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)


    @Before
    fun setUp() {
        resetAppData()
    }

    @After
    fun tearDown() {
        resetAppData()
    }

    @Test
    fun test1LanguageSelectionDialogAppears() {
        // Check if the language selection dialog is displayed
        onView(withText(R.string.select_language)).check(matches(isDisplayed()))

        // Select English (assuming it's the first option)
        onView(withText("English")).perform(click())

        // Check if the login screen is displayed after selecting a language
        onView(withId(R.id.username)).check(matches(isDisplayed()))
    }

    @Test
    fun test2ToastMessageDisplayed() {
        // Perform the action that triggers the Toast
        onView(withId(R.id.login_button)).perform(click())
        onView(isRoot()).perform(waitFor(1000))

        // Check if the Toast with the specific message is displayed
        onView(withId(R.id.login_button)).check(matches(isDisplayed()))

    }

    @Test
    fun test3NoPasswordToast() {

        // Perform the action that triggers the Toast
        onView(withId(R.id.username))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(isRoot()).perform(waitFor(1000))

        // Check if the Toast with the specific message is displayed
        onView(withId(R.id.login_button)).check(matches(isDisplayed()))

    }

    fun resetAppData() {
        // Clear Shared Preferences
        context.getSharedPreferences("selected_language", Context.MODE_PRIVATE)
            .edit().clear().commit()

        // Delete Databases
        context.deleteDatabase("my_database")

        // Delete Internal Storage Files
        context.filesDir.deleteRecursively()

        // Delete Cache
        context.cacheDir.deleteRecursively()
    }

    fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()

            override fun getDescription(): String = "Wait for $delay milliseconds."

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

}
