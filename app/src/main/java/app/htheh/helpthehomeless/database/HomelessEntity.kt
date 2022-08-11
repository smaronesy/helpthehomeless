package app.htheh.helpthehomeless.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.htheh.helpthehomeless.model.Homeless
import java.util.*
import kotlin.math.exp

@Entity(tableName = "homeless_profile")
data class HomelessEntity(

    @PrimaryKey
    @ColumnInfo(name="email")
    var email: String = "",

    @ColumnInfo(name="logged_in_user")
    var loggedInUser: String?,

    @ColumnInfo(name="first_name")
    var firstName: String?,

    @ColumnInfo(name="last_name")
    var lastName: String?,

    @ColumnInfo(name="phone")
    var phone: String?,

    @ColumnInfo(name="short_bio")
    var shortBio: String?,

    @ColumnInfo(name="education_level")
    var educationLevel: String?,

    @ColumnInfo(name="years_of_exp")
    var yearsOfExp: String?,

    @ColumnInfo(name="exp_description")
    var expDescription: String?,

    @ColumnInfo(name="needs_home")
    var needsHome: Boolean?,

    @ColumnInfo(name="location_description")
    var approximateLocation: String?,

    @ColumnInfo(name="latitude")
    var latitude: Double?,

    @ColumnInfo(name="longitude")
    var longitude: Double?,

    @ColumnInfo(name="walk_score")
    var walkScore: Int?,

    @ColumnInfo(name="ws_logo_url")
    var wsLogoUrl: String?,

    @ColumnInfo(name="firebase_image_uri")
    var firebaseImageUri: String?,

    @ColumnInfo(name="image_uri")
    var imageUri: String?,

    @ColumnInfo(name="image_path")
    var imagePath: String?,

    @ColumnInfo(name="date_added")
    var dateAdded: String?,

    @ColumnInfo(name = "entry_id")
    val id: String = UUID.randomUUID().toString()

)

fun HomelessEntity.toHomeless(): Homeless {
    return Homeless(
        email = email,
        loggedInUser = loggedInUser,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        shortBio = shortBio,
        educationLevel = educationLevel,
        yearsOfExp = yearsOfExp,
        expDescription = expDescription,
        needsHome = needsHome,
        approximateLocation = approximateLocation,
        latitude = latitude,
        longitude = longitude,
        walkScore= walkScore,
        wsLogoUrl= wsLogoUrl,
        firebaseImageUri = firebaseImageUri,
        imageUri = imageUri,
        imagePath = imagePath,
        dateAdded = dateAdded
    )
}

fun Homeless.toHomelessEntity(): HomelessEntity {
    return HomelessEntity(
        email = email,
        loggedInUser = loggedInUser,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        shortBio = shortBio,
        educationLevel = educationLevel,
        yearsOfExp = yearsOfExp,
        expDescription = expDescription,
        needsHome = needsHome,
        approximateLocation = approximateLocation,
        latitude = latitude,
        longitude = longitude,
        walkScore= walkScore,
        wsLogoUrl= wsLogoUrl,
        firebaseImageUri = firebaseImageUri,
        imageUri = imageUri,
        imagePath = imagePath,
        dateAdded = dateAdded
    )
}

fun List<HomelessEntity>.asDomainModel(): List<Homeless> {
    return map {
        Homeless(
            email = it.email,
            loggedInUser = it.loggedInUser,
            firstName = it.firstName,
            lastName = it.lastName,
            phone = it.phone,
            shortBio = it.shortBio,
            educationLevel = it.educationLevel,
            yearsOfExp = it.yearsOfExp,
            expDescription = it.expDescription,
            needsHome = it.needsHome,
            approximateLocation = it.approximateLocation,
            latitude = it.latitude,
            longitude = it.longitude,
            walkScore= it.walkScore,
            wsLogoUrl= it.wsLogoUrl,
            firebaseImageUri = it.firebaseImageUri,
            imageUri = it.imageUri,
            imagePath = it.imagePath,
            dateAdded = it.dateAdded
        )
    }
}

fun List<Homeless>.asDatabaseObject(): Array<HomelessEntity> {
    return map {
        HomelessEntity(
            email = it.email,
            loggedInUser = it.loggedInUser,
            firstName = it.firstName,
            lastName = it.lastName,
            phone = it.phone,
            shortBio = it.shortBio,
            educationLevel = it.educationLevel,
            yearsOfExp = it.yearsOfExp,
            expDescription = it.expDescription,
            needsHome = it.needsHome,
            approximateLocation = it.approximateLocation,
            latitude = it.latitude,
            longitude = it.longitude,
            walkScore= it.walkScore,
            wsLogoUrl= it.wsLogoUrl,
            firebaseImageUri = it.firebaseImageUri,
            imageUri = it.imageUri,
            imagePath = it.imagePath,
            dateAdded = it.dateAdded
        )
    }.toTypedArray()
}