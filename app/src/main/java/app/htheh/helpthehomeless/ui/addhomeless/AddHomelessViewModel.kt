package app.htheh.helpthehomeless.ui.addhomeless

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.PointOfInterest

class AddHomelessViewModel : ViewModel() {

    val locationDescription = MutableLiveData<String>()
    val selectedLocationStr = MutableLiveData<String>()
    val selectedPOI = MutableLiveData<PointOfInterest>()
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()
    val fgLocationPermission = MutableLiveData<Boolean>()

}