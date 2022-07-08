package app.htheh.helpthehomeless.ui.addhomeless

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.PointOfInterest
import android.net.Uri

class AddHomelessViewModel : ViewModel() {

    val homelessEmail = MutableLiveData<String>()
    val homelessFirstName = MutableLiveData<String>()
    val homelessLastName = MutableLiveData<String>()
    val homelessPhone = MutableLiveData<String>()
    val needsShelter = MutableLiveData<Boolean>()
    val approximateLocation = MutableLiveData<String>()
    val latitude = MutableLiveData<Double>()
    val longitude = MutableLiveData<Double>()
    val photoURI = MutableLiveData<Uri>()
    val photoAbsolutePath = MutableLiveData<String>()
    val dateAdded = MutableLiveData<String>()

    val fgLocationPermission = MutableLiveData<Boolean>()
    val selectedLocationStr = MutableLiveData<String>()
    val selectedPOI = MutableLiveData<PointOfInterest>()

}