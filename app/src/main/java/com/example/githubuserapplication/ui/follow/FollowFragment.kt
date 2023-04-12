package com.example.githubuserapplication.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.ui.RecycleViewAdapter
import com.example.githubuserapplication.databinding.FragmentFollowBinding
import com.example.githubuserapplication.data.remote.response.ItemsItem

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val list = ArrayList<FavoriteUser>()
    private var position : Int = 0
    private var username : String = "user"

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowViewModel::class.java]
        followViewModel.user.observe(viewLifecycleOwner) { user ->
            setFollowData(user)
        }

        followViewModel.message.observe(viewLifecycleOwner) { message ->
            toastError(message)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.rvFollow.setHasFixedSize(true)

        arguments?.let {
            position = it.getInt(ARG_SECTION_NUMBER)
            username = it.getString(ARG_USERNAME).toString()
        }
        if (position == 1) {
            followViewModel.getFollowersUsers(username)
        }
        else {
            followViewModel.getFollowingUsers(username)
        }


    }

    private fun showRecyclerList() {
        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        val listRecycleViewAdapter = RecycleViewAdapter(list)
        binding.rvFollow.adapter = listRecycleViewAdapter
        listRecycleViewAdapter.setOnItemClickCallback(object :
            RecycleViewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteUser) {
                // do nothing
            }
        })
    }


    private fun setFollowData(consumerSearch: List<ItemsItem?>?) {
        if (consumerSearch != null) {
            binding.rvNoData.visibility = View.GONE
            for (item in consumerSearch) {
                val user = FavoriteUser(
                    item?.id,
                    item?.login.toString(),
                    item?.avatarUrl
                )
                list.add(user)
            }
            showRecyclerList()
        }
        else {
            binding.rvNoData.visibility = View.VISIBLE
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun toastError(message: String?) {
        Toast.makeText(this.context, "Error: $message", Toast.LENGTH_SHORT).show()
    }


}