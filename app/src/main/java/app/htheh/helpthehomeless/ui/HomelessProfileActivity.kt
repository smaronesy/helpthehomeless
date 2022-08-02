package app.htheh.helpthehomeless.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
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

        Picasso.get().load(homeless?.wsLogoUrl).into(binding.walkScoreLogo)

        if(homeless?.imagePath != null){
            val takenPhoto = BitmapFactory.decodeFile(homeless.imagePath)
            val imageUriFromBitmap = getImageUri(takenPhoto)
            Picasso.get().load(imageUriFromBitmap).into(binding.profileImage)
        } else if(homeless?.imageUri != null) {
            Picasso.get().load(homeless?.imageUri).into(binding.profileImage)
        }
    }

    fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(this.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }
}