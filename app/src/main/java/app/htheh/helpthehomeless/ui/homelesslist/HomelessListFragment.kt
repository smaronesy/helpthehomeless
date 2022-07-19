package app.htheh.helpthehomeless.ui.homelesslist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.htheh.helpthehomeless.R
import app.htheh.helpthehomeless.databinding.FragmentHomelessListBinding
import app.htheh.helpthehomeless.repository.Filter
import app.htheh.helpthehomeless.ui.HomelessProfileActivity

class HomelessListFragment : Fragment() {

    private var _binding: FragmentHomelessListBinding? = null
    private lateinit var homelessListViewModel: HomelessListViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homelessListViewModel =
            ViewModelProvider(this).get(HomelessListViewModel::class.java)

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
        binding.fab.setOnClickListener{
            this.findNavController().navigate(HomelessListFragmentDirections.actionAddHomeless())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        homelessListViewModel.updateHomelessWithFilter(
            when(item.itemId) {
                R.id.show_week_menu -> Filter.WEEK
                R.id.show_today_menu -> Filter.TODAY
                else -> Filter.SAVED
            })
        return true
    }
}
private const val REQUEST_CODE_DEVICE_LOCATION_SETTINGS = 27
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val LOCATION_PERMISSION_INDEX = 1