package app.htheh.helpthehomeless.repository

import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.Result

interface HomelessDataSource {
    suspend fun addHomeless(homeless: HomelessEntity, encodedAddress: String)
    suspend fun addHomelessList(hlList: List<HomelessEntity>)
    suspend fun getHomelessByEmail(email: String): Result<HomelessEntity>
    suspend fun deleteAllReminders()
}