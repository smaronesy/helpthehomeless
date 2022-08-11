package app.htheh.helpthehomeless

import android.app.Application
import app.htheh.helpthehomeless.database.HomelessDatabase
import app.htheh.helpthehomeless.datasource.repository.HomelessRemoteRepository
import app.htheh.helpthehomeless.ui.addhomeless.AddHomelessViewModel
import app.htheh.helpthehomeless.ui.homelesslist.HomelessListViewModel
import app.htheh.helpthehomeless.ui.homelesspersonprofile.HomelessProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class HomelessApp : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                HomelessListViewModel(
                    get(), get()
                )
            }
            

            /**
             * single: indicates a singleton (only one instance)
             * factory: indicates a factory (defines a new instance everytime)
             */
            //Declare singleton definitions to be later injected using by inject()

            single {
                AddHomelessViewModel(
                    get(),
                    get()
                )
                HomelessProfileViewModel(
                    get(),
                    get()
                )
            }

            single {
                HomelessRemoteRepository()
            }

            single {
                HomelessDatabase.getInstance(get()).homelessDao
            }
        }

        startKoin {
            androidContext(this@HomelessApp)
            modules(listOf(myModule))
        }
    }
}