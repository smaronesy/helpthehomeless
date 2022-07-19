package app.htheh.helpthehomeless.ui.addhomeless

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.PointOfInterest
import android.net.Uri
import androidx.lifecycle.viewModelScope
import app.htheh.helpthehomeless.database.HomelessDatabase
import app.htheh.helpthehomeless.database.toHomelessEntity
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.repository.HomelessLocalRepository
import app.htheh.helpthehomeless.ui.HomelessViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddHomelessViewModel(application: Application) : HomelessViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val homelessEmail = MutableLiveData<String>()
    val homelessFirstName = MutableLiveData<String>()
    val homelessLastName = MutableLiveData<String>()
    val homelessPhone = MutableLiveData<String>()
    val needsShelter = MutableLiveData<Boolean>()
    val approximateLocation = MutableLiveData<String>()
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()
    val walkScore = MutableLiveData<Int>()
    val photoURI = MutableLiveData<Uri>()
    val photoAbsolutePath = MutableLiveData<String>()
    val dateAdded = MutableLiveData<String>()

    val fgLocationPermission = MutableLiveData<Boolean>()
    val selectedLocationStr = MutableLiveData<String>()
    val selectedPOI = MutableLiveData<PointOfInterest>()

    private val database = HomelessDatabase.getInstance(application)
    private val homelessLocalRepository = HomelessLocalRepository(application, database.homelessDao)

    val homeleesses = homelessLocalRepository.homelesses

    fun addHomeless(hl: Homeless, encodedAddress: String){
        viewModelScope.launch {
            homelessLocalRepository.addHomeless(hl.toHomelessEntity(), encodedAddress)
        }
    }
}