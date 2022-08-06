package app.htheh.helpthehomeless.ui.addhomeless.experience

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentExperienceBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.addhomeless.AddHomelessViewModel
import app.htheh.helpthehomeless.ui.addhomeless.selectlocation.SelectHomelessLocationFragmentArgs
import org.koin.android.ext.android.inject

class ExperienceFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var binding: FragmentExperienceBinding
    val addHomelessViewModel: AddHomelessViewModel by inject()
    private lateinit var homeLess: Homeless
    var selected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExperienceBinding.inflate(inflater, container, false)

        homeLess = SelectHomelessLocationFragmentArgs.fromBundle(arguments!!).homeless
        homeLess.yearsOfExp = addHomelessViewModel.yearsOfExp.value
        homeLess.expDescription = addHomelessViewModel.expDescription.value

        val spinner: Spinner = binding.experienceSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.experience_in_years,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this

        binding.nextToSelectLocation.setOnClickListener {
            if(selected){
                this.findNavController().navigate(ExperienceFragmentDirections.actionSelectLocation(homeLess))
            } else {
                Toast.makeText(this.requireContext(), "Please select years of experience", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        addHomelessViewModel.yearsOfExp.value = parent.getItemAtPosition(pos) as String?
        selected = true
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
        selected = false
    }

}