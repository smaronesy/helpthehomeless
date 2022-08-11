package app.htheh.helpthehomeless.ui.homelesslist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.htheh.helpthehomeless.databinding.FragmentHomelessListBinding
import app.htheh.helpthehomeless.ui.homelesspersonprofile.HomelessProfileActivity
import com.google.firebase.database.DatabaseReference
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomelessListFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var database: DatabaseReference

    private var _binding: FragmentHomelessListBinding? = null
    private val homelessListViewModel: HomelessListViewModel by viewModel()

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

        layoutManager = LinearLayoutManager(context);
        binding.homelessRecycler.layoutManager = layoutManager;

        val dividerItemDecoration = DividerItemDecoration(
            binding.homelessRecycler.getContext(),
            layoutManager.getOrientation()
        )

        binding.homelessRecycler.addItemDecoration(dividerItemDecoration)


        homelessListViewModel.navigateToHomelessDetails.observe(viewLifecycleOwner, Observer {homeless -> homeless?.let {
            var profileIntent = Intent(this.requireActivity(), HomelessProfileActivity::class.java).apply {
                putExtra("homeless", it)
            }
            startActivity(profileIntent)
            homelessListViewModel.onHomelessDetailNavigated()
        }})

        homelessListViewModel.initializeListeningToFirebaseDB()

        homelessListViewModel.homelessIndividuals.observe(viewLifecycleOwner, Observer {
            adapater.submitList(it)
        })

        setHasOptionsMenu(true)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hlAddFab.setOnClickListener{
            this.findNavController().navigate(HomelessListFragmentDirections.actionToDisclaimer())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}