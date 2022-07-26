package app.htheh.helpthehomeless.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import app.htheh.helpthehomeless.api.WalkScoreApi
import app.htheh.helpthehomeless.api.parseWalkScoreJsonResult
import app.htheh.helpthehomeless.database.HomelessDao
import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.Result
import app.htheh.helpthehomeless.database.asDomainModel
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.utils.Constants
import app.htheh.helpthehomeless.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*

enum class Filter { SAVED, WEEK, TODAY}

open class HomelessLocalRepository(
    private val homelessDao: HomelessDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
        Calendar.getInstance().time)

    private val _filter = MutableLiveData<Filter>()
    val filter
        get() = _filter

    val walkScore = MutableLiveData<Int>()

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

    suspend fun addHomelessList(hlList: List<HomelessEntity>) {
        withContext(Dispatchers.IO) {
            homelessDao.insertList(*hlList.toTypedArray())
        }
    }

    suspend fun addHomeless(homeless: HomelessEntity, encodedAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = WalkScoreApi.retrofitService.getProperties(Constants.WALK_SCORE_API_KEY,
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

//    suspend fun getWalkScore(homeless: Homeless, encodedAddress: String) {
//        withContext(Dispatchers.IO) {
//            try {
//                val response = WalkScoreApi.retrofitService.getProperties(Constants.API_KEY,
//                    homeless.latitude!!, homeless.longitude!!, encodedAddress, "json", 1, 1).awaitResponse()
//                val jsonBody = JSONObject(response.body())
//                walkScore.postValue(parseWalkScoreJsonResult(jsonBody))
//                println("WALK SCORE IS " + jsonBody)
//            } catch (ex: CancellationException) {
//                throw ex // Must let the CancellationException propagate
//            } catch (e: Exception) {
//                Log.e("API CALL ERROR", e.message.toString())
//            }
//        }
//    }

    suspend fun getHomelessByEmail(email: String): Result<HomelessEntity> =
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                try {
                    val homeless = homelessDao.get(email)
                    if (homeless != null) {
                        return@withContext Result.Success(homeless)
                    } else {
                        return@withContext Result.Error("homeless not found!")
                    }
                } catch (e: Exception) {
                    return@withContext Result.Error(e.localizedMessage)
                }
            }
        }

//    override suspend fun deleteAllReminders() {
//        TODO("Not yet implemented")
//    }

}