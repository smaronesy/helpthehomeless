package app.htheh.helpthehomeless.ui.homelesslist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.htheh.helpthehomeless.database.asDatabaseObject
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.datasource.repository.Filter
import app.htheh.helpthehomeless.datasource.repository.HomelessLocalRepository
import app.htheh.helpthehomeless.ui.HomelessViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomelessListViewModel(application: Application, private val homelessLocalRepository: HomelessLocalRepository) : HomelessViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    val homelesses = homelessLocalRepository.homelesses

    init {
        homelessLocalRepository.filter.value = Filter.SAVED
    }

    fun updateHomelessWithFilter(filter: Filter) {
        println("FILTER IS " + filter)
        homelessLocalRepository.filter.value = filter
        println("_filter IS " + homelessLocalRepository.filter.value)
    }

    fun addListOfHomelesses(hlList: List<Homeless>){
        viewModelScope.launch {
            homelessLocalRepository.addHomelessList(hlList.asDatabaseObject().toList())
        }
    }

    private val _navigateToHomelessDetails = MutableLiveData<Homeless>()
    val navigateToHomelessDetails
        get() = _navigateToHomelessDetails

    fun onHomelessClicked(homeless: Homeless){
        _navigateToHomelessDetails.value = homeless
    }

    fun onHomelessDetailNavigated() {
        _navigateToHomelessDetails.value = null
    }

}