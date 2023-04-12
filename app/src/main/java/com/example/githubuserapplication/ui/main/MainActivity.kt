package com.example.githubuserapplication.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapplication.R
import com.example.githubuserapplication.data.local.entity.FavoriteUser
import com.example.githubuserapplication.data.local.preferences.SettingPreferences
import com.example.githubuserapplication.databinding.ActivityMainBinding
import com.example.githubuserapplication.data.remote.response.ItemsItem
import com.example.githubuserapplication.ui.RecycleViewAdapter
import com.example.githubuserapplication.ui.detail.DetailActivity
import com.example.githubuserapplication.ui.favorite.FavoriteActivity
import com.example.githubuserapplication.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<FavoriteUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = SettingPreferences.getInstance(dataStore)
        supportActionBar?.title = "Github User Search"

        val mainViewModel = ViewModelProvider(this,MainViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.user.observe(this) { user ->
            setSearchData(user)
        }

        mainViewModel.message.observe(this) { message ->
            toastError(message)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.getThemeSettings().observe(this) {isActive ->
            if (isActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.edSearch.setTextColor(resources.getColor(R.color.primaryTextColor))
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.edSearch.setTextColor(resources.getColor(R.color.secondaryTextColor))
            }
        }

        binding.rvSearch.setHasFixedSize(true)
        binding.edSearch.setOnKeyListener(View.OnKeyListener {
                _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                list.clear()
                val inputSearch = binding.edSearch.text.toString()
                mainViewModel.findUser(inputSearch)
                hideKeyboard(binding.root)
                return@OnKeyListener true
            }
            false
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu1 -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                true
            }
            R.id.menu2 -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }

    private fun showRecyclerList() {
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        val listRecycleViewAdapter = RecycleViewAdapter(list)
        binding.rvSearch.adapter = listRecycleViewAdapter
        listRecycleViewAdapter.setOnItemClickCallback(object :
            RecycleViewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteUser) {
                val intentDetail = Intent(this@MainActivity, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.USER, data)
                startActivity(intentDetail)

            }
        })
    }

    private fun setSearchData(consumerSearch: List<ItemsItem?>?) {
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

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun toastError(message: String?) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

}