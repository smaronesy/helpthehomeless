package app.htheh.helpthehomeless.geofence

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.Result.Success
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.datasource.repository.HomelessRemoteRepository
import app.htheh.helpthehomeless.ui.addhomeless.savephoto.UploadHomelessPhotoFragment.Companion.ACTION_GEOFENCE_EVENT
import app.htheh.helpthehomeless.utils.sendNotification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {

    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }
    }

    override fun onHandleWork(intent: Intent) {

        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent!!.hasError()) {
                val errorMessage = errorMessage(this, geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }

            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.v(TAG, this.getString(R.string.geofence_entered))
                when {
                    geofencingEvent.triggeringGeofences!!.isNotEmpty() ->
                        geofenceIterator(geofencingEvent.triggeringGeofences!!)
                    else -> {
                        Log.e(TAG, "No Geofence Trigger Found! Abort mission!")
                        return
                    }
                }
            }
        }
    }

    // Loop through the geofence so all notification are sent
    private fun geofenceIterator(triggeringGeofences: List<Geofence>) {
        println("GEO List Size: " + triggeringGeofences.size)
        for(geo in triggeringGeofences){
            sendNotification(geo)
        }
    }

    private fun sendNotification(triggeringGeofence: Geofence) {
        val requestId = triggeringGeofence.requestId

        //Get the local repository instance
        val homelessRemoteRepository: HomelessRemoteRepository by inject()
//        Interaction to the repository has to be through a coroutine scope
        CoroutineScope(coroutineContext).launch(SupervisorJob()) {
            //get the homeless with the request id (email)
            val result = homelessRemoteRepository.getHomelessByEmail(requestId)
            if (result is Success<HomelessEntity>) {
                val homelessEntity = result.data
                //send a notification to the user with the homeless details
                sendNotification(
                    this@GeofenceTransitionsJobIntentService, Homeless(
                        homelessEntity.email,
                        homelessEntity.loggedInUser,
                        homelessEntity.firstName,
                        homelessEntity.lastName,
                        homelessEntity.phone,
                        homelessEntity.shortBio,
                        homelessEntity.educationLevel,
                        homelessEntity.educationDetails,
                        homelessEntity.yearsOfExp,
                        homelessEntity.expDescription,
                        homelessEntity.needsHome,
                        homelessEntity.approximateLocation,
                        homelessEntity.latitude,
                        homelessEntity.longitude,
                        homelessEntity.walkScore?.toInt(),
                        homelessEntity.wsLogoUrl,
                        homelessEntity.firebaseImageUri,
                        homelessEntity.imageUri,
                        homelessEntity.imagePath,
                        homelessEntity.dateAdded
                    )
                )
            }
        }
    }
}

private const val TAG = "GeofenceReceiver"