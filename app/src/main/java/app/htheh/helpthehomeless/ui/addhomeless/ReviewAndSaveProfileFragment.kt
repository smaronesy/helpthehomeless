package app.htheh.helpthehomeless.ui.addhomeless

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.BuildConfig
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentReviewAndSaveProfileBinding
import app.htheh.helpthehomeless.geofence.GeofenceBroadcastReceiver
import app.htheh.helpthehomeless.geofence.GeofencingConstants
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.addhomeless.savephoto.UploadHomelessPhotoFragment
import app.htheh.helpthehomeless.utils.getEncodedAddress
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.locationreminders.savereminder.*
import org.koin.android.ext.android.inject

private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val LOCATION_PERMISSION_INDEX = 1

class ReviewAndSaveProfileFragment : Fragment() {

    val addHomelessViewModel: AddHomelessViewModel by inject()
    private lateinit var binding: FragmentReviewAndSaveProfileBinding
    private lateinit var homeLess: Homeless

    private lateinit var geofencingClient: GeofencingClient

    //Check if API level is 29 and above
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.Q

    //Check if API level is 30 and above
    private val running30OrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.R

    val running31OrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.S

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(requireContext(), GeofenceBroadcastReceiver::class.java)
        intent.action = UploadHomelessPhotoFragment.ACTION_GEOFENCE_EVENT
        if(running31OrLater) {
            PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeLess = ReviewAndSaveProfileFragmentArgs.fromBundle(arguments!!).homeless

        // Inflate the layout for this fragment
        binding = FragmentReviewAndSaveProfileBinding.inflate(inflater, container, false)

        binding.homeless = homeLess

        if(homeLess?.imagePath != null){
            val takenPhoto = BitmapFactory.decodeFile(homeLess.imagePath)
            binding.ivProfileImage.setImageBitmap(takenPhoto)
        } else if(homeLess?.imageUri != null) {
            binding.ivProfileImage.setImageURI(Uri.parse(homeLess.imageUri))
        }

        geofencingClient = LocationServices.getGeofencingClient(this.requireActivity())

        addHomelessViewModel.geofencingItemSaved.observe(viewLifecycleOwner, Observer {
            if(it){
                addGeoFence()
                addHomelessViewModel.geofencingItemSaved.value = false
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveProfile.setOnClickListener {

            geofencingClient = LocationServices.getGeofencingClient(this.requireActivity())

            if(runningQOrLater) {
                if(running30OrLater) {
                    if(!foregroundLocationPermissionApproved(requireContext())){
                        requestForegroundLocationPermissions(this)
                    }
                    if (!backgroundLocationPermissionApproved(requireContext(), runningQOrLater)) {
                        requestBackgroundLocationPermissions(this)
                    }

                    if(backgroundLocationPermissionApproved(this.requireContext(), runningQOrLater)) {
                        checkDeviceLocationSettingsAndStartGeofence()
                    }

                } else {

                    if(!foregroundLocationPermissionApproved(requireContext())
                        || !backgroundLocationPermissionApproved(this.requireContext(), runningQOrLater)
                    ) {
                        requestForegroundAndBackgroundLocationPermissions(this, runningQOrLater)
                    } else {
                        checkDeviceLocationSettingsAndStartGeofence()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        println("Permission code IS: " + PackageManager.PERMISSION_DENIED)
        println("grantResults: " + grantResults[0])
        if (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE && grantResults.size > 0 ) {
            if (grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_GRANTED) {
                checkDeviceLocationSettingsAndStartGeofence()
            }
        } else if (grantResults.isEmpty() || grantResults.size < 2 ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            ((requestCode == REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    || requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE) &&
                    grantResults[LOCATION_PERMISSION_INDEX] ==
                    PackageManager.PERMISSION_DENIED)) {
            Snackbar.make(
                binding.root,
                R.string.gep_permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.settings) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
        }
    }

    private fun checkDeviceLocationSettingsAndStartGeofence(resolve:Boolean = true) {
        val locationSettingsResponseTask =
            getLocationSettingsResponseTask(this)

        locationSettingsResponseTask.addOnFailureListener { exception ->
            requestDeviceLocationPermissions(this, exception, resolve)
        }

        locationSettingsResponseTask.addOnCompleteListener {
            if ( it.isSuccessful ) {
                addHomelessToDB()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun addHomelessToDB() {

        // 1) save the reminder to the local db
        val encodedAddress = getEncodedAddress(this.requireActivity().application, homeLess)

        // get walk score and save homeless to database
        addHomelessViewModel.addHomeless(homeLess, encodedAddress)

    }

    private fun addGeoFence() {
        val geofence = Geofence.Builder()
            .setRequestId(homeLess.email)
            .setCircularRegion(
                homeLess.latitude!!,
                homeLess.longitude!!,
                GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
            addOnSuccessListener {
                if (activity != null) {
                    Toast.makeText(
                        activity, R.string.geofences_added,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                Log.e("Add Geofence", geofence.requestId)
            }
            addOnFailureListener {
                if (activity != null) {
                    Toast.makeText(
                        requireActivity(), R.string.geofences_not_added,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if ((it.message != null)) {
                    Log.w(ContentValues.TAG, it.message.toString())
                }
            }
        }

        // Navigate to Homeless List Screen
        this.findNavController()
            .navigate(ReviewAndSaveProfileFragmentDirections.actionToHomelessList())
    }

}