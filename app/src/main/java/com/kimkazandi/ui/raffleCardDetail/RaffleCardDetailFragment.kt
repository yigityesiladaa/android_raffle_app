package com.kimkazandi.ui.raffleCardDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kimkazandi.R
import com.kimkazandi.common.utils.Utils
import com.kimkazandi.databinding.FragmentRaffleCardDetailBinding
import com.kimkazandi.models.FollowedRaffle
import com.kimkazandi.models.Raffle

class RaffleCardDetailFragment : Fragment() {

    private var _binding: FragmentRaffleCardDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var raffleCardDetailViewModel: RaffleCardDetailViewModel
    private var href: String? = null
    private var groupName: String? = null
    private var raffle: Raffle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            href = it.getString("href")
            groupName = it.getString("groupName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRaffleCardDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
        listenEvents()
    }

    private fun init() {
        raffleCardDetailViewModel = ViewModelProvider(this)[RaffleCardDetailViewModel::class.java]
        raffleCardDetailViewModel.setContext(requireContext())
        if(href != null && groupName != null){
            Thread{
                raffleCardDetailViewModel.getRaffleDetailByHref(href!!,groupName!!)
                raffleCardDetailViewModel.initializeFollowStatus(href!!)
            }.start()
        }

    }

    private fun registerEvents() {
        binding.btnFollow.setOnClickListener(btnFollowClickListener)
    }

    private fun setViewValues(raffleDetail: Raffle){
        binding.txtRaffleTitle.text = raffleDetail.title
        binding.txtRaffleDescription.text = raffleDetail.description
        binding.txtRaffleDate.text = raffleDetail.raffleDate
        binding.txtAdvertDate.text = raffleDetail.advertDate
        binding.txtMinSpend.text = raffleDetail.minSpend
        binding.txtTotalGiftCount.text = raffleDetail.totalGiftCount
        binding.txtTotalGiftAmount.text = raffleDetail.totalGiftAmount
        binding.txtStartDate.text = raffleDetail.startDate
        binding.txtLastJoinDate.text = raffleDetail.lastJoinDate
        Glide.with(requireContext()).load(raffleDetail.imageUrl).into(binding.imgRaffleImageView)
    }

    private val btnFollowClickListener = View.OnClickListener {
        raffle?.let {
            val followedRaffle = FollowedRaffle(null,it.href)
            Thread{
                raffleCardDetailViewModel.changeFollowStatus(followedRaffle)
            }.start()
        }
    }

    private fun onLoading(){
        binding.pbRaffleDetail.visibility = View.VISIBLE
        binding.btnFollow.visibility = View.GONE
        binding.tablesLayout.visibility = View.GONE
    }

    private fun listenEvents() {
        raffleCardDetailViewModel.state.observe(viewLifecycleOwner){baseState->
            Utils.checkState(activity,baseState,onLoading()){
                binding.pbRaffleDetail.visibility = View.GONE
                binding.btnFollow.visibility = View.VISIBLE
                binding.tablesLayout.visibility = View.VISIBLE
            }
        }

        raffleCardDetailViewModel.isFollowedRaffle.observe(viewLifecycleOwner){
            if(it){
                binding.btnFollow.text = requireContext().getString(R.string.unfollow_text)
            }else{
                binding.btnFollow.text = requireContext().getText(R.string.follow_text)
            }
        }

        raffleCardDetailViewModel.raffleDetail.observe(viewLifecycleOwner){
            raffle = it
            setViewValues(it)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}