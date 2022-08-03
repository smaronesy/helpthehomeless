package app.htheh.helpthehomeless.ui.disclaimer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentDisclaimerBinding
import app.htheh.helpthehomeless.ui.homelesslist.HomelessListFragmentDirections

/**
 * A simple [Fragment] subclass.
 * Use the [DisclaimerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DisclaimerFragment : Fragment() {

    lateinit var binding: FragmentDisclaimerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDisclaimerBinding.inflate(inflater, container, false)

        binding.agreeButton.setOnClickListener{
            this.findNavController().navigate(DisclaimerFragmentDirections.actionAddHomeless())
        }

        return binding.root
    }

}