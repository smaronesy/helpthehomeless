package app.htheh.helpthehomeless.ui.povertystats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.htheh.helpthehomeless.R

class PovertyStatsFragment : Fragment() {

    companion object {
        fun newInstance() = PovertyStatsFragment()
    }

    private lateinit var viewModel: PovertyStatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poverty_stats, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PovertyStatsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}