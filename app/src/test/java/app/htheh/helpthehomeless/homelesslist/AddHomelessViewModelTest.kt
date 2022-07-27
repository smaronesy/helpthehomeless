package app.htheh.helpthehomeless.homelesslist

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.htheh.helpthehomeless.MainCoroutineRule
import app.htheh.helpthehomeless.database.HomelessDao
import app.htheh.helpthehomeless.database.HomelessDatabase
import app.htheh.helpthehomeless.database.HomelessEntity
import app.htheh.helpthehomeless.database.toHomeless
import app.htheh.helpthehomeless.datasource.repository.FakeHomelessLocalRepository
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.addhomeless.AddHomelessViewModel
import app.htheh.helpthehomeless.ui.homelesslist.HomelessListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class AddHomelessViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var addHomelessViewModel: AddHomelessViewModel
    private lateinit var fakeHomelessLocalRepository: FakeHomelessLocalRepository
    private lateinit var dao: HomelessDao

    private val hl1 = HomelessEntity(
        "a@a.com", "a",
        "b", "1",
        true, "walmart",
        23.1, 87.2,
        24, "imageUri",
        "imageePath", "12-12-12")

    private val hl2 = HomelessEntity(
        "b@a.com", "b",
        "b", "1",
        true, "walmart",
        23.1, 87.2,
        24, "imageUri",
        "imageePath", "12-12-12")


    private val homelessList = mutableListOf(hl1, hl2)

    @Before
    fun setup(){

        val database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HomelessDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.homelessDao

        fakeHomelessLocalRepository = FakeHomelessLocalRepository(dao, homelessList)
        addHomelessViewModel = AddHomelessViewModel(ApplicationProvider.getApplicationContext(), fakeHomelessLocalRepository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun getHomelessTest() {

        addHomelessViewModel.getHomeless(hl1.email)
        val hl_returned = (addHomelessViewModel.homeless.value as HomelessEntity)

        Assert.assertThat(hl_returned, CoreMatchers.`is`(hl1))
    }

    @Test
    fun addHomelessTest() {

        val hl = HomelessEntity(
            "b@a.com", "b",
            "b", "1",
            true, "walmart",
            23.1, 87.2,
            24, "imageUri",
            "imagePath", "12-12-12")

        addHomelessViewModel.addHomeless(hl.toHomeless(), "encodedAddress")
        addHomelessViewModel.getHomeless(hl.email)
        val hl_returned = (addHomelessViewModel.homeless.value as HomelessEntity)

        Assert.assertThat(hl_returned.email, CoreMatchers.`is`(hl.email))
    }


}