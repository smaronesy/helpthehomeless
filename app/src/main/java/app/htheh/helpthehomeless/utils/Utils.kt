package app.htheh.helpthehomeless.utils

import android.app.Application
import android.location.Address
import android.location.Geocoder
import app.htheh.helpthehomeless.model.Homeless
import java.net.URLEncoder
import java.util.*

fun getEncodedAddress(application: Application, homeLess: Homeless): String {

    println ("Latitude is " + homeLess.latitude + "Longitude is " + homeLess.longitude)

    val geoCoder = Geocoder(application, Locale.getDefault())
    val addresses: List<Address> =
        geoCoder.getFromLocation(homeLess.latitude!!, homeLess.longitude!!, 1)

    // everything is returned as string
    val address = addresses.get(0).getAddressLine(0)
    val city = addresses.get(0).getLocality()
    val state = addresses.get(0).getAdminArea()
    val stateAbbreviated = States().getStateAbbreviation(state)
    val postalCode = addresses.get(0).getPostalCode()

    //        val country = addresses.get(0).getCountryName()
    //        val knownName = addresses.get(0).getFeatureName()

    val fullAddress = address + " " + city + " " + stateAbbreviated + " " + postalCode
    val encodedAddress: String = URLEncoder.encode(fullAddress, "utf-8")
    return encodedAddress
}
