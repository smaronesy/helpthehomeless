package app.htheh.helpthehomeless.ui.addhomeless.shortbio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentAddHomelessBinding
import app.htheh.helpthehomeless.databinding.FragmentBioBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.addhomeless.AddHomelessViewModel
import app.htheh.helpthehomeless.ui.addhomeless.selectlocation.SelectHomelessLocationFragmentArgs
import org.koin.android.ext.android.inject

class BioFragment : Fragment() {

    lateinit var binding: FragmentBioBinding
    val addHomelessViewModel: AddHomelessViewModel by inject()
    private lateinit var homeLess: Homeless

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBioBinding.inflate(inflater, container, false)

        homeLess = SelectHomelessLocationFragmentArgs.fromBundle(arguments!!).homeless

        homeLess.shortBio  = addHomelessViewModel.shortBio.value

        binding.bioTextView.text = "Please provide a brief bio for " + homeLess.firstName

        binding.nextToEducation.setOnClickListener{
            this.findNavController().navigate(BioFragmentDirections.actionToEducation(homeLess))
        }

        return binding.root
    }
}