package app.htheh.helpthehomeless.repository

import app.htheh.helpthehomeless.database.HomelessEntity

interface HomelessDataSource {
    suspend fun getHomelesses(): Result<List<HomelessEntity>>
    suspend fun addHomeless(homeless: HomelessEntity)
    suspend fun addHomelessList(hlList: List<HomelessEntity>)
    suspend fun getHomeleessByEmail(email: String): Result<HomelessEntity>
    suspend fun deleteAllReminders()
}