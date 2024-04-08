package ba.unsa.etf.rma.cineaste

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyRightOf
import androidx.test.espresso.assertion.PositionAssertions.isLeftAlignedWith
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntentInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    private fun withImage(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Drawable does not contain image with id: $id")
        }

        override fun matchesSafely(item: View): Boolean {
            val context: Context = item.context
            val bitmap: Bitmap? = context.getDrawable(id)?.toBitmap()
            return item is ImageView && item.drawable.toBitmap().sameAs(bitmap)
        }
    }

    @Test
    fun testDetailActivityInstantiation() {
        val runDetails = Intent(ApplicationProvider.getApplicationContext(), MovieDetailActivity::class.java)
        runDetails.putExtra("movie_title","Pride and prejudice")
        launchActivity<MovieDetailActivity>(runDetails)

        onView(withId(R.id.movie_title)).check(matches(withText("Pride and prejudice")))
        onView(withId(R.id.movie_genre)).check(matches(withText("drama")))
        onView(withId(R.id.movie_overview)).check(matches(withSubstring("overcome their own pride and prejudice")))
        onView(withId(R.id.movie_poster)).check(matches(withImage(R.drawable.drama)))
    }

    @Test
    fun testLinksIntent() {
        Intents.init()

        val runDetails = Intent(ApplicationProvider.getApplicationContext(), MovieDetailActivity::class.java)
        runDetails.putExtra("movie_title","Pride and prejudice")
        launchActivity<MovieDetailActivity>(runDetails)

        onView(withId(R.id.movie_website)).perform(click())
        Intents.intended(hasAction(Intent.ACTION_VIEW))

        Intents.release()
    }

    @Test
    fun testLayoutDetailsActivity() {
        val runDetails = Intent(ApplicationProvider.getApplicationContext(), MovieDetailActivity::class.java)
        runDetails.putExtra("movie_title","Pride and prejudice")
        launchActivity<MovieDetailActivity>(runDetails)

        onView(withId(R.id.movie_poster))
            .check(isCompletelyLeftOf(withId(R.id.movie_title)))
        onView(withId(R.id.movie_release_date))
            .check(isCompletelyBelow(withId(R.id.movie_title)))
        onView(withId(R.id.movie_release_date))
            .check(isCompletelyRightOf(withId(R.id.movie_poster)))
        onView(withId(R.id.movie_genre))
            .check(isCompletelyBelow(withId(R.id.movie_release_date)))
        onView(withId(R.id.movie_genre))
            .check(isLeftAlignedWith(withId(R.id.movie_release_date)))
        onView(withId(R.id.movie_website))
            .check(isCompletelyBelow(withId(R.id.movie_poster)))
        onView(withId(R.id.movie_overview))
            .check(isCompletelyBelow(withId(R.id.movie_website)))
            .check(isLeftAlignedWith(withId(R.id.movie_website)))
    }
}