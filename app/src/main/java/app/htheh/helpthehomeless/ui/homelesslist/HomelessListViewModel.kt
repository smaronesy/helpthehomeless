package app.htheh.helpthehomeless.ui.homelesslist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.htheh.helpthehomeless.database.asDatabaseObject
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.datasource.repository.Filter
import app.htheh.helpthehomeless.datasource.repository.HomelessRemoteRepository
import app.htheh.helpthehomeless.ui.HomelessViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomelessListViewModel(application: Application, private val homelessRemoteRepository: HomelessRemoteRepository) : HomelessViewModel(application) {

    private var viewModelJob = Job()
    var database: DatabaseReference

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

//    val homelesses = homelessLocalRepository.homelesses

    var homelessIndividuals = homelessRemoteRepository.homelessIndividuals

    init {
        homelessRemoteRepository.filter.value = Filter.SAVED
        database = Firebase.database.reference
    }

    fun initializeListeningToFirebaseDB(){
        homelessRemoteRepository.initializeListeningToFirebaseDB()
    }

    fun updateHomelessWithFilter(filter: Filter) {
        println("FILTER IS " + filter)
        homelessRemoteRepository.filter.value = filter
        println("_filter IS " + homelessRemoteRepository.filter.value)
    }

    fun addListOfHomelesses(hlList: List<Homeless>){
        viewModelScope.launch {
            homelessRemoteRepository.addHomelessList(hlList.asDatabaseObject().toList())
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