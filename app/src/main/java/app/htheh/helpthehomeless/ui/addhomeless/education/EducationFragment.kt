package app.htheh.helpthehomeless.ui.addhomeless.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentAddHomelessBindingImpl
import app.htheh.helpthehomeless.databinding.FragmentEducationBinding
import app.htheh.helpthehomeless.databinding.FragmentExperienceBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.ui.addhomeless.AddHomelessViewModel
import app.htheh.helpthehomeless.ui.addhomeless.selectlocation.SelectHomelessLocationFragmentArgs
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EducationFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentEducationBinding
    private lateinit var homeLess: Homeless
    var selected: Boolean = false

    val addHomelessViewModel: AddHomelessViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEducationBinding.inflate(inflater, container, false)

        homeLess = SelectHomelessLocationFragmentArgs.fromBundle(arguments!!).homeless
        homeLess.educationLevel = addHomelessViewModel.educationLevel.value

        val spinner: Spinner = binding.educationLevelSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.education_level_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this

        binding.nextToExperience.setOnClickListener {
            if(selected){
                this.findNavController().navigate(EducationFragmentDirections.actionToExperience(homeLess))
            } else {
                Toast.makeText(this.requireContext(), "Please select an education level", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        println("PPPP" + parent.getItemAtPosition(pos))
        addHomelessViewModel.educationLevel.value = parent.getItemAtPosition(pos) as String?
        selected = true
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
        selected = false
    }

}