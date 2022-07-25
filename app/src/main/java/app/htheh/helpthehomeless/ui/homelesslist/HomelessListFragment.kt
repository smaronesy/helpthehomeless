package app.htheh.helpthehomeless.ui.homelesslist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.databinding.FragmentHomelessListBinding
import app.htheh.helpthehomeless.ui.HomelessProfileActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomelessListFragment : Fragment() {

    private var _binding: FragmentHomelessListBinding? = null
    val homelessListViewModel: HomelessListViewModel by viewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomelessListBinding.inflate(inflater, container, false)

        binding.listViewModel = homelessListViewModel

        binding.lifecycleOwner = this

        val root: View = binding.root

        val adapater = RecyclerViewAdapater(HomelessListener {
            homelessListViewModel.onHomelessClicked(it)
        })

        binding.homelessRecycler.adapter = adapater

        homelessListViewModel.navigateToHomelessDetails.observe(viewLifecycleOwner, Observer {homeless -> homeless?.let {
//            this.findNavController().navigate(HomelessListFragmentDirections.actionShowProfile(it))
            var profileIntent = Intent(this.requireActivity(), HomelessProfileActivity::class.java).apply {
                putExtra("homeless", it)
            }
            startActivity(profileIntent)
            homelessListViewModel.onHomelessDetailNavigated()
        }})

        homelessListViewModel.homelesses.observe(viewLifecycleOwner, Observer {
            adapater.submitList(it)
        })

        setHasOptionsMenu(true)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hlAddFab.setOnClickListener{
            this.findNavController().navigate(HomelessListFragmentDirections.actionAddHomeless())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}