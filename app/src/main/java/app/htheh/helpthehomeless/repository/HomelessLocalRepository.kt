package app.htheh.helpthehomeless.repository

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import app.htheh.helpthehomeless.api.WalkScoreApi
import app.htheh.helpthehomeless.api.parseWalkScoreJsonResult
import app.htheh.helpthehomeless.database.HomelessDao
import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.asDomainModel
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.utils.Constants
import app.htheh.helpthehomeless.utils.States
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*
import app.htheh.helpthehomeless.database.Result
import app.htheh.helpthehomeless.utils.wrapEspressoIdlingResource

enum class Filter { SAVED, WEEK, TODAY}

open class HomelessLocalRepository(
    private val application: Application,
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

    override suspend fun addHomeless(homeless: HomelessEntity, encodedAddress: String) {

        withContext(Dispatchers.IO) {
            try {
                val response = WalkScoreApi.retrofitService.getProperties(Constants.API_KEY,
                    homeless.latitude!!, homeless.longitude!!, encodedAddress, "json", 1, 1).awaitResponse()
                val jsonBody = JSONObject(response.body())
                homeless.walkScore =  parseWalkScoreJsonResult(jsonBody)
                println("WALK SCORE IS " + jsonBody)
            } catch (e: Exception) {
                Log.e("API CALL ERROR", e.message.toString())
            }
            homelessDao.insert(homeless)
        }

    }

    override suspend fun getHomelessByEmail(email: String): Result<HomelessEntity> =
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                try {
                    val reminder = homelessDao.get(email)
                    if (reminder != null) {
                        return@withContext Result.Success(reminder)
                    } else {
                        return@withContext Result.Error("Reminder not found!")
                    }
                } catch (e: Exception) {
                    return@withContext Result.Error(e.localizedMessage)
                }
            }
        }

    override suspend fun deleteAllReminders() {
        TODO("Not yet implemented")
    }

}