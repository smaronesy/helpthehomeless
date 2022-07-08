package app.htheh.helpthehomeless.ui.addhomeless

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.databinding.FragmentAddHomelessBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class AddHomelessFragment : Fragment() {

    companion object {
        fun newInstance() = AddHomelessFragment()
    }

    private lateinit var addHomelessViewModel: AddHomelessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddHomelessBinding.inflate(inflater, container, false)

        addHomelessViewModel = ViewModelProvider(this).get(AddHomelessViewModel::class.java)

        binding.lifecycleOwner = this

        val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
            Calendar.getInstance().time)

        addHomelessViewModel.homelessEmail.value = binding.etEmail.text.toString()
        addHomelessViewModel.homelessFirstName.value = binding.etFirstName.text.toString()
        addHomelessViewModel.homelessLastName.value = binding.etLastName.text.toString()
        addHomelessViewModel.homelessPhone.value = binding.etPhone.text.toString()
        addHomelessViewModel.dateAdded.value = today

        val homeless = Homeless(
            addHomelessViewModel.homelessEmail.value!!, addHomelessViewModel.homelessFirstName.value,
            addHomelessViewModel.homelessLastName.value, addHomelessViewModel.homelessPhone.value,
            addHomelessViewModel.needsShelter.value, addHomelessViewModel.approximateLocation.value,
            addHomelessViewModel.latitude.value, addHomelessViewModel.longitude.value,
            addHomelessViewModel.photoURI.value.toString(), addHomelessViewModel.photoAbsolutePath.value,
            addHomelessViewModel.dateAdded.value
        )

        binding.ahContinue.setOnClickListener {
            this.findNavController().navigate(AddHomelessFragmentDirections.actionSelectLocation(homeless))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}