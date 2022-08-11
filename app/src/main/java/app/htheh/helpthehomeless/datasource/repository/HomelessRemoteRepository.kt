package app.htheh.helpthehomeless.datasource.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import app.htheh.helpthehomeless.api.WalkScoreApi
import app.htheh.helpthehomeless.api.parseWalkScoreJsonResult
import app.htheh.helpthehomeless.api.parseWalkScoreLogoUrl
import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.Result
import app.htheh.helpthehomeless.database.toHomelessEntity
import app.htheh.helpthehomeless.datasource.HomelessDataSource
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*

enum class Filter { SAVED, WEEK, TODAY}

open class HomelessRemoteRepository: HomelessDataSource {

    val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
        Calendar.getInstance().time)

    private val _filter = MutableLiveData<Filter>()
    val filter
        get() = _filter

    val walkScore = MutableLiveData<Int>()
    var database: DatabaseReference

    init {
        database = Firebase.database.reference
    }

    var homelessIndividuals = MutableLiveData<List<Homeless>>()

    // listen to firebase data changes
    val homelessPersonListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val homelessPersons = dataSnapshot.child("homeless_individuals_db")
            val homelessList = mutableListOf<Homeless>()
            for (child in homelessPersons.children) {
                println("DB Snap1 " + child.child("lastName").value)

                val email = child.child("email").value as String
                val loggedInUser =  child.child("loggedInUser").value as String?
                val firstName = child.child("firstName").value as String?
                val lastName = child.child("firstName").value as String?
                val phone = child.child("phone").value as String?
                val shortBio = child.child("shortBio").value as String?
                val educationLevel = child.child("educationLevel").value as String?
                val yearsOfExp = child.child("yearsOfExp").value as String?
                val expDescription = child.child("expDescription").value as String?
                val needsHome = child.child("needsHome").value as Boolean?
                val approximateLocation = child.child("approximateLocation").value as String?
                val latitude = child.child("latitude").value as Double?
                val longitude = child.child("longitude").value as Double?
                val walkScore =  child.child("walkScore").value as Long?
                val wsLogoUrl = child.child("wsLogoUrl").value as String?
                val firebaseImageUri = child.child("firebaseImageUri").value as String?
                val imageUri = child.child("imageUri").value as String?
                val imagePath = child.child("imagePath").value as String?
                val dateAdded = child.child("dateAdded").value as String?
                val id = child.child("id").value as String

                val homelessIndividual = Homeless(
                    email, loggedInUser, firstName, lastName, phone, shortBio,
                    educationLevel, yearsOfExp, expDescription,
                    needsHome, approximateLocation, latitude,
                    longitude, walkScore?.toInt(), wsLogoUrl, firebaseImageUri,
                    imageUri, imagePath, dateAdded, id
                )
                homelessList.add(homelessIndividual)
            }
            homelessIndividuals.value = homelessList
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("REVIEW FRAGMENT", "loadPost:onCancelled", databaseError.toException())
        }
    }

    fun initializeListeningToFirebaseDB(){
        database.addValueEventListener(homelessPersonListener)
    }

    suspend fun addHomelessPersonToFirebaseDb(homeless: Homeless, encodedAddress: String){
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
            database.child("homeless_individuals_db").child(homeless.id).setValue(homeless)
        }

    }

    fun removeHomelessPersonToFirebaseDb(homeless: Homeless){
        database.child("homeless_individuals_db").child(homeless.id).removeValue()
    }

    override suspend fun getHomelessList(): Result<List<HomelessEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun addHomelessList(hlList: List<HomelessEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun addHomeless(homeless: HomelessEntity, encodedAddress: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getHomelessByEmail(email: String): Result<HomelessEntity> {
        var homelessPerson: HomelessEntity? = null
        if(homelessIndividuals.value != null){
            for(homeless in homelessIndividuals.value!!){
                if(homeless.email == email){
                    homelessPerson = homeless.toHomelessEntity()
                }
            }
        }

        try {
            if (homelessPerson != null) {
                return Result.Success(homelessPerson)
            } else {
                return Result.Error("homeless not found!")
            }
        } catch (e: Exception) {
            return Result.Error(e.localizedMessage)
        }
    }

    override suspend fun deleteHomelessByEmail(email: String) {
        TODO("Not yet implemented")
    }

}