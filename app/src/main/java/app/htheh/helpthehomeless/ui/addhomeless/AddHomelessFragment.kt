package app.htheh.helpthehomeless.ui.addhomeless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.databinding.FragmentAddHomelessBinding
import app.htheh.helpthehomeless.model.Homeless
import app.htheh.helpthehomeless.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class AddHomelessFragment : Fragment() {

    val addHomelessViewModel: AddHomelessViewModel by inject()
    private lateinit var binding: FragmentAddHomelessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddHomelessBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        addHomelessViewModel.loggedInUser.value = FirebaseAuth.getInstance().currentUser?.email

        val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
            Calendar.getInstance().time)
        addHomelessViewModel.dateAdded.value = today

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ahContinue.setOnClickListener {
            addHomelessViewModel.homelessEmail.value = binding.etEmail.text.toString()
            addHomelessViewModel.homelessFirstName.value = binding.etFirstName.text.toString()
            addHomelessViewModel.homelessLastName.value = binding.etLastName.text.toString()
            addHomelessViewModel.homelessPhone.value = binding.etPhone.text.toString()
            if(binding.etNeedsShelter.isChecked){
                addHomelessViewModel.needsShelter.value = true
            }
            val homeless = Homeless(
                addHomelessViewModel.homelessEmail.value!!, addHomelessViewModel.loggedInUser.value,
                addHomelessViewModel.homelessFirstName.value, addHomelessViewModel.homelessLastName.value,
                addHomelessViewModel.homelessPhone.value, addHomelessViewModel.shortBio.value,
                addHomelessViewModel.educationLevel.value, addHomelessViewModel.educationDetails.value,
                addHomelessViewModel.yearsOfExp.value, addHomelessViewModel.expDescription.value,
                addHomelessViewModel.needsShelter.value, addHomelessViewModel.approximateLocation.value,
                addHomelessViewModel.latitude.value, addHomelessViewModel.longitude.value,
                addHomelessViewModel.walkScore.value, addHomelessViewModel.wsLogoUrl.value,
                addHomelessViewModel.firebaseImageUri.value, addHomelessViewModel.photoURI.value.toString(),
                addHomelessViewModel.photoAbsolutePath.value, addHomelessViewModel.dateAdded.value
            )

            if(binding.etEmail.text.isNullOrBlank() || binding.etFirstName.text.isNullOrBlank()
                || binding.etLastName.text.isNullOrBlank() || binding.etPhone.text.isNullOrBlank()){
                Toast.makeText(this.requireContext(), "Please complete all of the fields", Toast.LENGTH_SHORT).show()
            } else {
                this.findNavController().navigate(AddHomelessFragmentDirections.actionToShortBio(homeless))
            }
        }
    }
}