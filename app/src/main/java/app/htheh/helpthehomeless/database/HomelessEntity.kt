package app.htheh.helpthehomeless.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.htheh.helpthehomeless.model.Homeless

@Entity(tableName = "homeless_profile")
data class HomelessEntity(

    @PrimaryKey
    @ColumnInfo(name="email")
    var email: String = "",

    @ColumnInfo(name="first_name")
    var firstName: String?,

    @ColumnInfo(name="last_name")
    var lastName: String?,

    @ColumnInfo(name="phone")
    var phone: String?,

    @ColumnInfo(name="needs_home")
    var needsHome: Boolean?,

    @ColumnInfo(name="location_description")
    var approximateLocation: String?,

    @ColumnInfo(name="latitude")
    var latitude: Double?,

    @ColumnInfo(name="longitude")
    var longitude: Double?,

    @ColumnInfo(name="image_uri")
    var imageUri: String?,

    @ColumnInfo(name="image_path")
    var imagePath: String?,

    @ColumnInfo(name="date_added")
    var dateAdded: String?
)

fun HomelessEntity.toHomeless(): Homeless {
    return Homeless(
        email = email,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        needsHome = needsHome,
        approximateLocation = approximateLocation,
        latitude = latitude,
        longitude = longitude,
        imageUri = imageUri,
        imagePath = imagePath,
        dateAdded = dateAdded
    )
}

fun Homeless.toHomelessEntity(): HomelessEntity {
    return HomelessEntity(
        email = email,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        needsHome = needsHome,
        approximateLocation = approximateLocation,
        latitude = latitude,
        longitude = longitude,
        imageUri = imageUri,
        imagePath = imagePath,
        dateAdded = dateAdded
    )
}

fun List<HomelessEntity>.asDomainModel(): List<Homeless> {
    return map {
        Homeless(
            email = it.email,
            firstName = it.firstName,
            lastName = it.lastName,
            phone = it.phone,
            needsHome = it.needsHome,
            approximateLocation = it.approximateLocation,
            latitude = it.latitude,
            longitude = it.longitude,
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
            firstName = it.firstName,
            lastName = it.lastName,
            phone = it.phone,
            needsHome = it.needsHome,
            approximateLocation = it.approximateLocation,
            latitude = it.latitude,
            longitude = it.longitude,
            imageUri = it.imageUri,
            imagePath = it.imagePath,
            dateAdded = it.dateAdded
        )
    }.toTypedArray()
}