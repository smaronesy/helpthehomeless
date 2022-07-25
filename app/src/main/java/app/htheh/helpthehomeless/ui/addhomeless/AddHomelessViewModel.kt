package app.htheh.helpthehomeless.ui.addhomeless

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.htheh.helpthehomeless.database.toHomelessEntity
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.repository.HomelessLocalRepository
import app.htheh.helpthehomeless.ui.HomelessViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddHomelessViewModel(application: Application, val homelessLocalRepository: HomelessLocalRepository) : HomelessViewModel(application) {

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

    val geofencingItemSaved = MutableLiveData<Boolean>()

    fun onClear() {

        homelessEmail.value = null
        homelessFirstName.value = null
        homelessLastName.value = null
        homelessPhone.value = null
        needsShelter.value = null
        approximateLocation.value = null
        latitude.value = null
        longitude.value = null
        walkScore.value = null
        photoURI.value = null
        photoAbsolutePath.value = null
        dateAdded.value = null
        fgLocationPermission.value = null
        selectedLocationStr.value = null
    }

//    private val database = HomelessDatabase.getInstance(application)
//    private val homelessLocalRepository = HomelessLocalRepository(application, database.homelessDao)

//    val homeleesses = homelessLocalRepository.homelesses

//    fun setWalkScore(hl: Homeless, encodedAddress: String){
//        viewModelScope.launch {
//            homelessLocalRepository.getWalkScore(hl, encodedAddress)
//            walkScore.value = homelessLocalRepository.walkScore.value
//        }
//    }

    fun addHomeless(hl: Homeless, encodedAddress: String){
        viewModelScope.launch {
            homelessLocalRepository.addHomeless(hl.toHomelessEntity(), encodedAddress)
            geofencingItemSaved.value = true
            onClear()
        }
    }

    /**
     * Validate the entered data and show error to the user if there's any invalid data
     */
}