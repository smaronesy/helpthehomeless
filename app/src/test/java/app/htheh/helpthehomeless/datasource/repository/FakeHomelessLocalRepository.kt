package app.htheh.helpthehomeless.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.htheh.helpthehomeless.database.*
import app.htheh.helpthehomeless.model.Homeless

class FakeHomelessLocalRepository(var dao: HomelessDao, var homelessList: MutableList<HomelessEntity> = mutableListOf()): HomelessLocalRepository(dao) {

    private var shouldReturnError = false

    override suspend fun getHomelessList(): Result<List<HomelessEntity>> {
        if(shouldReturnError){
            return Result.Error("Test Exception")
        }

        homelessList?.let {
            return Result.Success(ArrayList(it))
        }

        return Result.Error("No Homeless Profiles")
    }

    override suspend fun addHomelessList(hlList: List<HomelessEntity>) {
        homelessList.addAll(hlList)
    }

    override suspend fun addHomeless(homeless: HomelessEntity, encodedAddress: String) {
        homelessList.add(homeless)
    }

    override suspend fun getHomelessByEmail(email: String): Result<HomelessEntity> {
        if(shouldReturnError){
            return Result.Error("Test Exception")
        }

        for(hl in homelessList){
            if(hl.email == email){
                return Result.Success(hl)
            }
        }

        return Result.Error("No Homeless Profiles")
    }

    override suspend fun deleteHomelessByEmail(email: String) {
       for(hl in homelessList){
            if(hl.email == email){
                homelessList.remove(hl)
                break
            }
        }
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
}