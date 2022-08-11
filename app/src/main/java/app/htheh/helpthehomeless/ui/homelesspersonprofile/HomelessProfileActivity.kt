package app.htheh.helpthehomeless.ui.homelesspersonprofile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.ActivityHomelessProfileBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.AuthenticationActivity
import app.htheh.helpthehomeless.ui.HomelessActivity
import app.htheh.helpthehomeless.ui.addhomeless.AddHomelessViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject


class HomelessProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomelessProfileBinding
    val homelessProfileViewModel: HomelessProfileViewModel by inject()

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

        binding = ActivityHomelessProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeless = intent.extras?.get("homeless") as Homeless?

        binding.homeless = homeless

        // loading the profile image and walk score logo
//        Picasso.get().load(homeless?.wsLogoUrl).into(binding.walkScoreLl)
        Picasso.get().load(Uri.parse(homeless?.firebaseImageUri)).fit().centerInside()
            .error(R.drawable.ic_no_internet)
            .placeholder(R.drawable.ic_photo_library_128).into(binding.profileImage)

        binding.profileDeleteBtn.isVisible = homeless?.loggedInUser == FirebaseAuth.getInstance().currentUser?.email

        binding.profileDeleteBtn.setOnClickListener {
            homelessProfileViewModel.removeHomelessPersonToFirebaseDb(homeless!!)
            val intentToHome = Intent(this, HomelessActivity::class.java)
            startActivity(intentToHome)
        }

    }
}