package app.htheh.helpthehomeless.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Homeless(
    val email: String, val firstName: String,
    val lastName: String, val phone: String,
    val needsHome: Boolean, val dateAdded: String
) : Parcelable