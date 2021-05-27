package com.wildan.consumerapp.followTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.wildan.consumerapp.databinding.FragmentFollowBinding
import com.wildan.consumerapp.R
import com.wildan.consumerapp.dataHelper.Functions
import com.wildan.consumerapp.dataHelper.Functions.setToast
import com.wildan.consumerapp.detail.DetailActivity

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding?= null
    private val binding get() = _binding!!
    private lateinit var followerList : FollowListAdapter
    private lateinit var followViewModel: FollowViewModel
    private lateinit var username : String
    private lateinit var follow : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Functions.setVisibility(binding.progressFollow, true)
        followerList = FollowListAdapter()
        followerList.notifyDataSetChanged()

        binding.followRv.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = followerList
        }
        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowViewModel::class.java)
        if (arguments != null) {
            username = arguments?.getString(DetailActivity.USER_DETAIL).toString()
            follow = arguments?.getString(DetailActivity.USER_FOLLOW).toString()
        }
        followViewModel.setFollow(username, follow)
        followViewModel.getFollow().observe(this,{
            if (it != null) {
                followerList.setData(it)
                if (it.isEmpty()) {
                    binding.tvStatus.text = getString(R.string.no_followers)
                    Functions.setVisibility(binding.tvStatus, true)
                }
                else Functions.setVisibility(binding.tvStatus, false)
            }
            Functions.setVisibility(binding.progressFollow, false)
            Functions.setVisibility(binding.followRv, true)
        })
        followViewModel.getStatus().observe(this,{
            if (it != null) setToast(it, activity!!)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}