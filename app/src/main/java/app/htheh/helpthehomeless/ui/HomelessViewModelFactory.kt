package app.htheh.helpthehomeless.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class HomelessViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomelessViewModel::class.java)) {
            return HomelessViewModel(application) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class")
    }
}