package com.kimkazandi.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kimkazandi.R
import com.kimkazandi.common.utils.Utils
import com.kimkazandi.databinding.FragmentGroupDetailBinding
import com.kimkazandi.libraries.room.database.DatabaseConfig
import com.kimkazandi.listeners.ISharedGroupListener
import com.kimkazandi.models.Raffle

class SharedGroupFragment : Fragment(), ISharedGroupListener {

    private var _binding: FragmentGroupDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedGroupViewModel: SharedGroupViewModel
    private lateinit var customRafflesAdapter: CustomRafflesAdapter
    private var groupName = " "
    private var raffleCards = listOf<Raffle>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupName = it.getString("groupId", " ")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        listenEvents()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        sharedGroupViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application),
        )[SharedGroupViewModel::class.java]
        sharedGroupViewModel.checkAndUpdateData(groupName)
        val raffleDao = DatabaseConfig.getDatabase(requireContext()).raffleDao()
        val followsDao = DatabaseConfig.getDatabase(requireContext()).followsDao()
        sharedGroupViewModel.setDao(raffleDao, followsDao)
        customRafflesAdapter = CustomRafflesAdapter(requireContext(), this)
        setRecyclerViewAttrs()
        sharedGroupViewModel.setContext(requireContext())
        getRaffles()
    }

    private fun getRaffles() {
        Thread {
            sharedGroupViewModel.getAllRaffles(groupName)
        }.start()
    }

    private fun setRecyclerViewAttrs() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.groupsRecyclerView.layoutManager = layoutManager
        binding.groupsRecyclerView.adapter = customRafflesAdapter
    }

    private fun listenEvents() {

        sharedGroupViewModel.state.observe(viewLifecycleOwner) { baseState ->
            Utils.checkState(activity, baseState, onLoading()) {
                binding.pbGroups.visibility = View.GONE
                binding.groupsRecyclerView.visibility = View.VISIBLE
            }
        }
        sharedGroupViewModel.raffleCards.observe(viewLifecycleOwner) {
            raffleCards = it
            customRafflesAdapter.submitList(it)
        }

    }

    private fun onLoading() {
        binding.pbGroups.visibility = View.VISIBLE
        binding.groupsRecyclerView.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onItemClickListener(raffle: Raffle) {
        val bundle = Bundle()
        bundle.putString("href", raffle.href)
        bundle.putString("groupName", raffle.groupName)
        navController.navigate(R.id.action_group_detail_fragment_to_raffle_detail_fragment, bundle)
    }


}