package app.htheh.helpthehomeless.ui.homelesspersonprofile.eddetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.ActivityEdDetailsBinding
import app.htheh.helpthehomeless.model.Homeless

class EdDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEdDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEdDetailsBinding.inflate(layoutInflater)

        val homeless = intent.extras?.get("homeless") as Homeless?

        binding.edDetailContent.text = homeless?.educationDetails

        setContentView(binding.root)
    }
}