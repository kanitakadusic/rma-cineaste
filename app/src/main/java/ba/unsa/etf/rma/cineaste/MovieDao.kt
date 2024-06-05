package ba.unsa.etf.rma.cineaste

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    suspend fun readAll(): List<Movie>

    @Insert
    suspend fun insertAll(vararg movies: Movie)

    @Delete
    suspend fun deleteAll(vararg movies: Movie)

    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun findById(id: Int): Movie
}