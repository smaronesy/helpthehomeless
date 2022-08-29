package app.htheh.helpthehomeless.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@IgnoreExtraProperties
data class Homeless(
    var email: String, var loggedInUser: String?, var firstName: String?, var lastName: String?,
    var phone: String?, var shortBio: String?, var educationLevel: String?, var educationDetails: String?,
    var yearsOfExp: String?, var expDescription: String?, var needsHome: Boolean?,
    var approximateLocation: String?, var latitude: Double?, var longitude: Double?,
    var walkScore: Int?, var wsLogoUrl: String?, var firebaseImageUri: String?,  var imageUri: String?,
    var imagePath: String?, var dateAdded: String?, val id: String = UUID.randomUUID().toString()
) : Parcelable