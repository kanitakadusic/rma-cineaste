package ba.unsa.etf.rma.cineaste

import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.not
import org.junit.Test

class UnitTests {

    @Test
    fun testGetFavoriteMovies() {
        val movies = getFavoriteMovies()

        assertEquals(movies.size, 3)
        assertThat(movies, hasItem<Movie>(hasProperty("title", `is`("La La Land"))))
        assertThat(movies, not(hasItem<Movie>(hasProperty("title",`is`("Molly's Game")))))
    }

    @Test
    fun testGetRecentMovies() {
        val movies = getRecentMovies()

        assertEquals(movies.size, 3)
        assertThat(movies, hasItem<Movie>(hasProperty("title", `is`("Dune: Part Two"))))
        assertThat(movies, not(hasItem<Movie>(hasProperty("title",`is`("Good Will Hunting")))))
    }
}