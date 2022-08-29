package app.htheh.helpthehomeless.ui.homelesspersonprofile.expdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.ActivityEdDetailsBinding
import app.htheh.helpthehomeless.databinding.ActivityExpDetailsBinding
import app.htheh.helpthehomeless.databinding.FragmentExperienceBinding
import app.htheh.helpthehomeless.model.Homeless

class ExpDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpDetailsBinding.inflate(layoutInflater)

        val homeless = intent.extras?.get("homeless") as Homeless?

        binding.expDetailContent.text = homeless?.educationDetails

        setContentView(binding.root)
    }
}