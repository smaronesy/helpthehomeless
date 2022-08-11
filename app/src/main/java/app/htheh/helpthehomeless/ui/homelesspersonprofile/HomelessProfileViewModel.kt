package app.htheh.helpthehomeless.ui.homelesspersonprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import app.htheh.helpthehomeless.datasource.repository.HomelessRemoteRepository
import app.htheh.helpthehomeless.model.Homeless

class HomelessProfileViewModel(
    application: Application,
    val homelessRemoteRepository: HomelessRemoteRepository
    ): AndroidViewModel(application) {

    fun removeHomelessPersonToFirebaseDb(homeless: Homeless){
        homelessRemoteRepository.removeHomelessPersonToFirebaseDb(homeless)
    }
}