package app.htheh.helpthehomeless.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Homeless(
    var email: String, var firstName: String?, var lastName: String?,
    var phone: String?, var shortBio: String?, var educationLevel: String?,
    var yearsOfExp: String?, var expDescription: String?, var needsHome: Boolean?,
    var approximateLocation: String?, var latitude: Double?, var longitude: Double?,
    var walkScore: Int?, var wsLogoUrl: String?, var imageUri: String?,
    var imagePath: String?, var dateAdded: String?, val id: String = UUID.randomUUID().toString()
) : Parcelable