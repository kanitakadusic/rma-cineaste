package ba.unsa.etf.rma.cineaste

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyRightOf
import androidx.test.espresso.assertion.PositionAssertions.isLeftAlignedWith
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.unsa.etf.rma.cineaste.activities.MainActivity
import ba.unsa.etf.rma.cineaste.activities.DetailsActivity
import ba.unsa.etf.rma.cineaste.utils.getFavoriteMovies
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
        val runDetails = Intent(ApplicationProvider.getApplicationContext(), DetailsActivity::class.java)
        runDetails.putExtra("movie_title","Pride and prejudice")
        launchActivity<DetailsActivity>(runDetails)

        onView(withId(R.id.detailsTitle)).check(matches(withText("Pride and prejudice")))
        onView(withId(R.id.detailsGenre)).check(matches(withText("drama")))
        onView(withId(R.id.detailsOverview)).check(matches(withSubstring("overcome their own pride and prejudice")))
        onView(withId(R.id.detailsPoster)).check(matches(withImage(R.drawable.undefined)))
    }

    @Test
    fun testLinksIntent() {
        Intents.init()

        val runDetails = Intent(ApplicationProvider.getApplicationContext(), DetailsActivity::class.java)
        runDetails.putExtra("movie_title","Pride and prejudice")
        launchActivity<DetailsActivity>(runDetails)

        onView(withId(R.id.detailsWebsite)).perform(click())
        Intents.intended(hasAction(Intent.ACTION_VIEW))

        Intents.release()
    }

    @Test
    fun testLayoutDetailsActivity() {
        val runDetails = Intent(ApplicationProvider.getApplicationContext(), DetailsActivity::class.java)
        runDetails.putExtra("movie_title","Pride and prejudice")
        launchActivity<DetailsActivity>(runDetails)

        onView(withId(R.id.detailsPoster))
            .check(isCompletelyLeftOf(withId(R.id.detailsTitle)))
        onView(withId(R.id.detailsReleaseDate))
            .check(isCompletelyBelow(withId(R.id.detailsTitle)))
        onView(withId(R.id.detailsReleaseDate))
            .check(isCompletelyRightOf(withId(R.id.detailsPoster)))
        onView(withId(R.id.detailsGenre))
            .check(isCompletelyBelow(withId(R.id.detailsReleaseDate)))
        onView(withId(R.id.detailsGenre))
            .check(isLeftAlignedWith(withId(R.id.detailsReleaseDate)))
        onView(withId(R.id.detailsWebsite))
            .check(isCompletelyBelow(withId(R.id.detailsPoster)))
        onView(withId(R.id.detailsOverview))
            .check(isCompletelyBelow(withId(R.id.detailsWebsite)))
            .check(isLeftAlignedWith(withId(R.id.detailsWebsite)))
    }

    @Test
    fun testWebSearchAction() {
        Intents.init()

        val runDetails = Intent(ApplicationProvider.getApplicationContext(), DetailsActivity::class.java)
        runDetails.putExtra("movie_title","Pride and prejudice")
        launchActivity<DetailsActivity>(runDetails)

        onView(withId(R.id.detailsTitle)).perform(click())
        Intents.intended(hasExtra(SearchManager.QUERY, "Pride and prejudice" + " trailer"))

        Intents.release()
    }

    @Test
    fun testIntentActionSend() {
        val intent = Intent()
        intent.putExtra(Intent.EXTRA_TEXT, "Some text")
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.`package` = "ba.unsa.etf.rma.cineaste"

        launchActivity<MainActivity>(intent).use {
            onView(withId(R.id.searchText)).check(matches(withText("Some text")))
        }
    }

    @Test
    fun testFavoriteMoviesList() {
        val runApp = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        launchActivity<MainActivity>(runApp)

        val movies = getFavoriteMovies()

        for (movie in movies) {
            onView(withId(R.id.favoriteMovies))
                .perform(scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText(movie.title))))
        }
    }
}