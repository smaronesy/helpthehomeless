package app.htheh.helpthehomeless.ui.addhomeless

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentAddHomelessBinding
import app.htheh.helpthehomeless.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class AddHomelessFragment : Fragment() {

    companion object {
        fun newInstance() = AddHomelessFragment()
    }

    private lateinit var addProfileViewModel: AddHomelessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddHomelessBinding.inflate(inflater, container, false)

        addProfileViewModel = ViewModelProvider(this).get(AddHomelessViewModel::class.java)

        binding.lifecycleOwner = this

        val today = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
            Calendar.getInstance().time)

        binding.ahContinue.setOnClickListener {
            this.findNavController().navigate(AddHomelessFragmentDirections.actionSelectLocation())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}