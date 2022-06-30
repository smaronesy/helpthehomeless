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
    var firstName: String = "",

    @ColumnInfo(name="last_name")
    var lastName: String = "",

    @ColumnInfo(name="phone")
    var phone: String = "",

    @ColumnInfo(name="needs_home")
    var needsHome: Boolean = false,

    @ColumnInfo(name="date_added")
    var dateAdded: String = ""
)

fun List<HomelessEntity>.asDomainModel(): List<Homeless> {
    return map {
        Homeless(
            email = it.email,
            firstName = it.firstName,
            lastName = it.lastName,
            phone = it.phone,
            needsHome = it.needsHome,
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
            dateAdded = it.dateAdded
        )
    }.toTypedArray()
}