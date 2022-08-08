package app.htheh.helpthehomeless.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.ActivityHomelessProfileBinding
import app.htheh.helpthehomeless.model.Homeless
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class HomelessProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomelessProfileBinding

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

//        Use to get firebaseUri here
//        val storageRef = myFirebaseStorage.reference
//        storageRef.child(path).downloadUrl.addOnSuccessListener {
//            // Got the download URL for path
//            addHomelessViewModel.firebaseImageUri.value = it.toString()
//        }.addOnFailureListener {
//            // Handle any errors
//        }

        Picasso.get().load(homeless?.wsLogoUrl).into(binding.walkScoreLogo)
        Picasso.get().load(Uri.parse(homeless?.firebaseImageUri)).fit().centerInside()
            .rotate(90F)                    //if you want to rotate by 90 degrees
            .error(R.drawable.ic_no_internet)
            .placeholder(R.drawable.ic_photo_library_128).into(binding.profileImage)

    }
}