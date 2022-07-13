package app.htheh.helpthehomeless.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import app.htheh.helpthehomeless.database.HomelessDao
import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.asDomainModel
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

enum class Filter { SAVED, WEEK, TODAY}

open class HomelessLocalRepository(
    private val homelessDao: HomelessDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): HomelessDataSource {

    val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
        Calendar.getInstance().time)

    private val _filter = MutableLiveData<Filter>()
    val filter
        get() = _filter

    val homelesses: LiveData<List<Homeless>> =
        Transformations.switchMap(_filter) {
            // gets the date from last week, seven days ago
            val calender = Calendar.getInstance()
            calender.add(Calendar.DAY_OF_YEAR, -7)
            val week = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                .format(calender.time)
            println("FILTER IS" + _filter.value + week.toString())
            when(_filter.value) {
                Filter.TODAY -> Transformations.map(homelessDao.getTodayHomelesses(today)) { it.asDomainModel()}
                Filter.WEEK -> Transformations.map(homelessDao.getWeekHomelesses(week)) { it.asDomainModel()}
                else -> Transformations.map(homelessDao.getAllHomelesses()) { it.asDomainModel()}
            }
        }

    override suspend fun addHomelessList(hlList: List<HomelessEntity>) {
        withContext(Dispatchers.IO) {
            homelessDao.insertList(*hlList.toTypedArray())
        }
    }

    override suspend fun getHomelesses(): Result<List<HomelessEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun addHomeless(homeless: HomelessEntity) {
        withContext(Dispatchers.IO) {
            homelessDao.insert(homeless)
        }
    }

    override suspend fun getHomeleessByEmail(email: String): Result<HomelessEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }

}