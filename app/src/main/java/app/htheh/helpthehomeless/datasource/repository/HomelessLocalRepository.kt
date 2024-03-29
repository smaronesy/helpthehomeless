package app.htheh.helpthehomeless.datasource.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import app.htheh.helpthehomeless.api.WalkScoreApi
import app.htheh.helpthehomeless.api.parseWalkScoreJsonResult
import app.htheh.helpthehomeless.api.parseWalkScoreLogoUrl
import app.htheh.helpthehomeless.database.HomelessDao
import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.Result
import app.htheh.helpthehomeless.database.asDomainModel
import app.htheh.helpthehomeless.datasource.HomelessDataSource
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

class HomelessLocalRepository(
    private val homelessDao: HomelessDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): HomelessDataSource {

    val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
        Calendar.getInstance().time)

    private val _filter = MutableLiveData<Filter>()
    val filter
        get() = _filter

    val walkScore = MutableLiveData<Int>()

    val homelessIndividuals: LiveData<List<Homeless>> =
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

    override suspend fun getHomelessList(): Result<List<HomelessEntity>> {
        TODO("dadfd")
    }

    override suspend fun addHomelessList(hlList: List<HomelessEntity>) {
        withContext(Dispatchers.IO) {
            homelessDao.insertList(*hlList.toTypedArray())
        }
    }

    override suspend fun addHomeless(homeless: HomelessEntity, encodedAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = WalkScoreApi.retrofitService.getProperties(Constants.WALK_SCORE_API_KEY,
                    homeless.latitude!!, homeless.longitude!!, encodedAddress, "json", 1, 1).awaitResponse()
                val jsonBody = JSONObject(response.body())
                homeless.walkScore = parseWalkScoreJsonResult(jsonBody)
                homeless.wsLogoUrl = parseWalkScoreLogoUrl(jsonBody)
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

    override suspend fun deleteHomelessByEmail(email: String) {
        TODO("Not yet implemented")
    }
}