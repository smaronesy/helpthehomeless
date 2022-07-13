package app.htheh.helpthehomeless.repository

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import app.htheh.helpthehomeless.api.WalkScoreApi
import app.htheh.helpthehomeless.database.HomelessDao
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.utils.Constants
import app.htheh.helpthehomeless.utils.States
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class HomelessRemoteRepository(
    private val application: Application,
    private val latitude: MutableLiveData<Double>,
    private val longitude: MutableLiveData<Double>
){

    suspend fun getWalkScore() {

        val geoCoder = Geocoder(application, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latitude.value!!, longitude.value!!, 1)

        // everything is returned as string
        val address = addresses.get(0).getAddressLine(0)
        val city = addresses.get(0).getLocality()
        val state = addresses.get(0).getAdminArea()
        val stateAbbreviated = States().getStateAbbreviation(state)
        val postalCode = addresses.get(0).getPostalCode()

//        val country = addresses.get(0).getCountryName()
//        val knownName = addresses.get(0).getFeatureName()

        val fullAddress = address+" "+city+" "+stateAbbreviated+" "+postalCode
        val encodedAddress: String = java.net.URLEncoder.encode(fullAddress, "utf-8")


        withContext(Dispatchers.IO) {
            try {
                val walkScore = WalkScoreApi.retrofitService.getProperties(Constants.API_KEY,
                    latitude.value!!, longitude.value!!, encodedAddress )
                println("WALK SCORE IS" + walkScore)
            } catch (e: Exception) {
                Log.e("APICALL ERROR POD", e.message.toString())
            }
        }
    }
}