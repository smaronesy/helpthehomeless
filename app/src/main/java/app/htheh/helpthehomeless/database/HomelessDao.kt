package app.htheh.helpthehomeless.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HomelessDao {

    /**
     * Homeless Profile SQL Calls
     **/

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = HomelessEntity::class)
    fun insert(homeless: HomelessEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = HomelessEntity::class)
    fun insertList(vararg homeless: HomelessEntity)

    /**
     * Selects and returns the row that matches the supplied email, which is our key.
     *
     * @param key email to match
     */
    @Query("SELECT * from homeless_profile WHERE email = :email")
    fun get(email: String): HomelessEntity?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM homeless_profile")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *s
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM homeless_profile ORDER BY last_name DESC")
    fun getAllHomelesses(): LiveData<List<HomelessEntity>>

    @Query("SELECT * FROM homeless_profile WHERE date_added == :today ORDER BY date_added DESC")
    fun getTodayHomelesses(today: String): LiveData<List<HomelessEntity>>

    @Query("SELECT * FROM homeless_profile WHERE date_added <= :week ORDER BY date_added DESC")
    fun getWeekHomelesses(week: String): LiveData<List<HomelessEntity>>


    /**
     * add poverty stats data to the stats_table below
     */


}