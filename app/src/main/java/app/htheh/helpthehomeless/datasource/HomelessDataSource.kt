package app.htheh.helpthehomeless.datasource

import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.Result

interface HomelessDataSource {
    suspend fun getHomelessList(): Result<List<HomelessEntity>>
    suspend fun addHomelessList(hlList: List<HomelessEntity>)
    suspend fun addHomeless(homeless: HomelessEntity, encodedAddress: String)
    suspend fun getHomelessByEmail(email: String): Result<HomelessEntity>
    suspend fun deleteHomelessByEmail(email: String)
}