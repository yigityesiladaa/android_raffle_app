package com.kimkazandi.ui.follows

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kimkazandi.R
import com.kimkazandi.common.utils.Utils
import com.kimkazandi.databinding.FragmentFollowsBinding
import com.kimkazandi.libraries.room.database.DatabaseConfig
import com.kimkazandi.listeners.ISharedGroupListener
import com.kimkazandi.models.Raffle
import com.kimkazandi.ui.groups.CustomRafflesAdapter

class FollowsFragment : Fragment(), ISharedGroupListener {

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var followsViewModel: FollowsViewModel
    private lateinit var customRafflesAdapter: CustomRafflesAdapter
    private lateinit var navController: NavController
    private var followedRaffles = mutableListOf<Raffle>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        listenEvents()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        followsViewModel = ViewModelProvider(this)[FollowsViewModel::class.java]
        followsViewModel.setContext(requireContext())
        customRafflesAdapter = CustomRafflesAdapter(requireContext(),this)
        setDao()
        setRecyclerViewAttrs()
        getFollowedRaffles()
    }

    private fun listenEvents() {
        followsViewModel.state.observe(viewLifecycleOwner){baseState->
            Utils.checkState(activity,baseState,onLoading()){
                binding.pbFollows.visibility = View.GONE
                binding.followsRecyclerView.visibility = View.VISIBLE
            }
        }

        followsViewModel.raffles.observe(viewLifecycleOwner){
            followedRaffles = it
            customRafflesAdapter.submitList(it)
            binding.pbFollows.visibility = View.GONE
            binding.followsRecyclerView.visibility = View.VISIBLE
            if(it.isEmpty()){
                binding.txtNothingToShowYet.visibility = View.VISIBLE
            }else{
                binding.txtNothingToShowYet.visibility = View.GONE
            }
        }
    }

    private fun setDao(){
        val raffleDao = DatabaseConfig.getDatabase(requireContext()).raffleDao()
        val followsDao = DatabaseConfig.getDatabase(requireContext()).followsDao()
        followsViewModel.setDao(raffleDao,followsDao)
    }

    private fun onLoading(){
        binding.pbFollows.visibility = View.VISIBLE
        binding.followsRecyclerView.visibility = View.GONE
    }

    private fun setRecyclerViewAttrs(){
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.followsRecyclerView.layoutManager = layoutManager
        binding.followsRecyclerView.adapter = customRafflesAdapter
    }

    private fun getFollowedRaffles(){
        Thread{
            followsViewModel.getFollowedRaffles()
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClickListener(raffle: Raffle) {
        val bundle = Bundle()
        bundle.putString("href",raffle.href)
        bundle.putString("groupName",raffle.groupName)
        navController.navigate(R.id.action_nav_follows_to_raffleCardDetailFragment,bundle)
    }



}