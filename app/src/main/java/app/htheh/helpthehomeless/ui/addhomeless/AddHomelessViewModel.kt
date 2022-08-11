package app.htheh.helpthehomeless.ui.addhomeless

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.Result
import app.htheh.helpthehomeless.database.toHomelessEntity
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.datasource.repository.HomelessRemoteRepository
import app.htheh.helpthehomeless.ui.HomelessViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddHomelessViewModel(
    application: Application,
    val homelessRemoteRepository: HomelessRemoteRepository
    ) : HomelessViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val homelessEmail = MutableLiveData<String>()
    val loggedInUser = MutableLiveData<String>()
    val homelessFirstName = MutableLiveData<String>()
    val homelessLastName = MutableLiveData<String>()
    val homelessPhone = MutableLiveData<String>()
    val shortBio = MutableLiveData<String>()
    val educationLevel = MutableLiveData<String>()
    val yearsOfExp = MutableLiveData<String>()
    val expDescription = MutableLiveData<String>()
    val needsShelter = MutableLiveData<Boolean>()
    val approximateLocation = MutableLiveData<String>()
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()
    val walkScore = MutableLiveData<Int>()
    val wsLogoUrl = MutableLiveData<String>()
    val firebaseImageUri = MutableLiveData<String>()
    val photoURI = MutableLiveData<Uri>()
    val photoAbsolutePath = MutableLiveData<String>()
    val dateAdded = MutableLiveData<String>()

    val fgLocationPermission = MutableLiveData<Boolean>()
    val selectedLocationStr = MutableLiveData<String>()

    val geofencingItemSaved = MutableLiveData<Boolean>()

    val homeless = MutableLiveData<HomelessEntity>()

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
        firebaseImageUri.value = null
        photoURI.value = null
        photoAbsolutePath.value = null
        dateAdded.value = null
        fgLocationPermission.value = null
        selectedLocationStr.value = null
    }

    fun addHomelessPersonToFirebaseDb(homeless: Homeless, encodedAddress: String) {
        viewModelScope.launch {
            homelessRemoteRepository.addHomelessPersonToFirebaseDb(homeless, encodedAddress)
            geofencingItemSaved.value = true
            onClear()
        }
    }

    fun addHomeless(hl: Homeless, encodedAddress: String){
        viewModelScope.launch {
            homelessRemoteRepository.addHomeless(hl.toHomelessEntity(), encodedAddress)
            geofencingItemSaved.value = true
            onClear()
        }
    }

    fun getHomeless(email: String){
        viewModelScope.launch {
            val result = homelessRemoteRepository.getHomelessByEmail(email)
            if (result is Result.Success<HomelessEntity>) {
                homeless.value = result.data
            }
        }
    }

    /**
     * Validate the entered data and show error to the user if there's any invalid data
     */
}