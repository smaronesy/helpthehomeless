package app.htheh.helpthehomeless.ui.addhomeless.selectlocation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.BuildConfig
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentSelectHomelessLocationBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.addhomeless.AddHomelessViewModel
import app.htheh.helpthehomeless.ui.homelesslist.HomelessListFragmentDirections
import app.htheh.helpthehomeless.utils.setDisplayHomeAsUpEnabled
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.locationreminders.savereminder.foregroundLocationPermissionApproved
import com.udacity.project4.locationreminders.savereminder.getLocationSettingsResponseTask
import com.udacity.project4.locationreminders.savereminder.requestDeviceLocationPermissions
import com.udacity.project4.locationreminders.savereminder.requestForegroundLocationPermissions
import kotlinx.android.synthetic.main.fragment_select_homeless_location.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [SelectHomelessLocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectHomelessLocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var addHomelessViewModel: AddHomelessViewModel
    private lateinit var binding: FragmentSelectHomelessLocationBinding
    private lateinit var homeLess: Homeless

    private lateinit var lastKnownLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var locationCallback: LocationCallback

    private var coordinates: LatLng? = null

    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.Q

    //Check if API level is 30 and above
    private val running30OrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.R

    var marker: Marker? = null
    var pioName: String? = null

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            || requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE) {
            if (grantResults.size > 0 &&
                grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_GRANTED) {
                addHomelessViewModel.fgLocationPermission.value = true
                if(map != null){
                    getDeviceLocation()
                    map.isMyLocationEnabled = true
                    map.uiSettings.isMyLocationButtonEnabled  = true
                }
            }
        } else if (grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE &&
                    grantResults[LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED)) {

            Snackbar.make(
                binding.root,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.settings) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
        }

        addHomelessViewModel.fgLocationPermission.value = foregroundLocationPermissionApproved(this.requireContext())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DEVICE_LOCATION_SETTINGS) {
            // We don't rely on the result code, but just check the location setting again
            checkDeviceLocationSettings(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addHomelessViewModel = ViewModelProvider(this).get(AddHomelessViewModel::class.java)

        binding = FragmentSelectHomelessLocationBinding.inflate(inflater, container, false)
        binding.addHomelessViewModel = addHomelessViewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

        homeLess = SelectHomelessLocationFragmentArgs.fromBundle(arguments!!).homeless

        // add the map setup implementation
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult?) {
//                locationResult ?: return
//                for (location in locationResult.locations){
//                    // Update UI with location data
//                    lastKnownLocation = location
//                }
//            }
//        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        checkDeviceLocationSettings()
    }

    /**
     * Starts the permission check and Geofence process only if the Geofence associated with the
     * current hint isn't yet active.
     */
    private fun checkPermissions() {
        if (!foregroundLocationPermissionApproved(this.requireContext())) {
            requestForegroundLocationPermissions(this)
        }

        if (foregroundLocationPermissionApproved(this.requireContext())) {
            addHomelessViewModel.fgLocationPermission.value = true
            getDeviceLocation()
        }
    }

    /**
     *  Uses the Location Client to check the current state of location settings, and gives the user
     *  the opportunity to turn on location services within our app.
     **/
    private fun checkDeviceLocationSettings (resolve:Boolean = true) {
        val locationSettingsResponseTask =
            getLocationSettingsResponseTask(this)

        locationSettingsResponseTask.addOnFailureListener { exception ->
            requestDeviceLocationPermissions(this, exception, resolve)
        }

        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                getDeviceLocation()
                Log.d(TAG, "Device Location Permission Granted")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // zoom to the user location after taking his permission
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        getDeviceLocation()

        //  put a marker to location that the user selected
        // Add marker on the selected location
        setMapLongClick(map)

        setPoiClick(map)

        //  add style to the map
        setMapStyle(map)

        //  call this function after the user confirms on the selected location
        onLocationSelected()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (addHomelessViewModel.fgLocationPermission.value == true) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                requestForegroundLocationPermissions(this)
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (addHomelessViewModel.fgLocationPermission.value == true) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        if (task.result != null) {
                            lastKnownLocation = task.result
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), 15f))
                        } else {
                            getDeviceLocation()
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                            .newLatLngZoom(LatLng(32.7, 117.2), 12f))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun setMapLongClick(map:GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A Snippet is Additional text that's displayed below the title.
            marker?.remove()
            coordinates = latLng
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            )
        }
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            pioName = poi.name
            coordinates = poi.latLng
            marker?.remove()
            marker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            marker!!.showInfoWindow()
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this.requireContext(),
                    R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun onLocationSelected() {
        //        When the user confirms on the selected location,
        //         send back the selected location details to the view model
        //         and navigate back to the previous fragment to save the reminder and add the geofence
        binding.confirmButton.setOnClickListener {
            if(coordinates != null) {
                // A Snippet is Additional text that's displayed below the title.
                addHomelessViewModel.latitude.value = coordinates!!.latitude
                addHomelessViewModel.longitude.value = coordinates!!.longitude

                homeLess.latitude = coordinates!!.latitude
                homeLess.longitude = coordinates!!.longitude
                if(pioName != null){
                    addHomelessViewModel.selectedLocationStr.value = pioName
                } else {
                    addHomelessViewModel.selectedLocationStr.value = "Random Location"
                }
                this.findNavController().navigate(SelectHomelessLocationFragmentDirections.actionUploadPhoto(homeLess))
            } else {
                Toast.makeText(this.requireContext(), "Please select a location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
//private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
private const val REQUEST_CODE_DEVICE_LOCATION_SETTINGS = 27
private const val TAG = "RemindersMainActivity"
private const val LOCATION_PERMISSION_INDEX = 0