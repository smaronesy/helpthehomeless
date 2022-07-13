package app.htheh.helpthehomeless.ui.homelessprofile

import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentHomelessProfileBinding
import java.util.*

class HomelessProfileFragment : Fragment() {

    companion object {
        fun newInstance() = HomelessProfileFragment()
    }

    private lateinit var viewModel: HomelessProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val  binding = FragmentHomelessProfileBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val homeless = HomelessProfileFragmentArgs.fromBundle(arguments!!).selectedHomeless
        binding.homeless = homeless

        if(homeless.imagePath != null){
            val takenPhoto = BitmapFactory.decodeFile(homeless.imagePath)
            binding.profileImage.setImageBitmap(takenPhoto)
        } else if(homeless.imageUri != null) {
            binding.profileImage.setImageURI(Uri.parse(homeless.imageUri))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomelessProfileViewModel::class.java)
        // TODO: Use the ViewModel


    }
}