package app.htheh.helpthehomeless.ui

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import app.htheh.helpthehomeless.databinding.ActivityHomelessProfileBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.homelesslist.HomelessListViewModel
import app.htheh.helpthehomeless.utils.getEncodedAddress
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomelessProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomelessProfileBinding
//    val homelessViewModel: HomelessListViewModel by viewModel()

    companion object {
        private const val EXTRA_HomelessDataItem = "homeless"

        // receive the reminder object after the user clicks on the notification
        fun newIntent(context: Context, homeless: Homeless): Intent {
            val intent = Intent(context, HomelessProfileActivity::class.java)
            intent.putExtra(EXTRA_HomelessDataItem, homeless)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_homeless_profile)
        binding = ActivityHomelessProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeless = intent.extras?.get("homeless") as Homeless?

//        val encodedAddress = getEncodedAddress(this.application, homeless!!)
//        homelessViewModel.setWalkScore(homeless!!, encodedAddress)

//        homelessViewModel.walkScore.observe(this, Observer {
//            homeless.walkScore = it
//        })

        binding.homeless = homeless

        if(homeless?.imagePath != null){
            val takenPhoto = BitmapFactory.decodeFile(homeless.imagePath)
            binding.profileImage.setImageBitmap(takenPhoto)
        } else if(homeless?.imageUri != null) {
            binding.profileImage.setImageURI(Uri.parse(homeless.imageUri))
        }
    }
}