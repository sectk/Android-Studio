package com.example.laravelapi.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import com.example.laravelapi.model.DataMahasiswa
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.laravelapi.R
import com.example.laravelapi.activity.tambahData.TambahMahasiswaActivity
import com.example.laravelapi.adapter.MahasiswaAdapter
import com.example.laravelapi.config.NetworkConfig
import com.example.laravelapi.databinding.ActivityMainBinding
import com.example.laravelapi.model.ResponseListMahasiswa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
        val btnTambah = binding.btnTambah
        btnTambah.setOnClickListener {
            val tambah = Intent(this, TambahMahasiswaActivity::class.java)
            startActivity(tambah)
        }

        val swipeRefresh = binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener(this)

        val appbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.app_bar_search)
        setSupportActionBar(appbar)

        getMahasiswa()
    }

    private fun getMahasiswa() {
        NetworkConfig().getServices()
            .getMahasiswa()
            .enqueue(object : Callback<ResponseListMahasiswa> {
                override fun onResponse(
                    call: Call<ResponseListMahasiswa>,
                    response: Response<ResponseListMahasiswa>
                ) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    if (response.isSuccessful) {
                        val receiverDatas = response.body()?.data
                        setToAdapter(receiverDatas)
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                override fun onFailure(call: Call<ResponseListMahasiswa>, t: Throwable) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    Log.d("Retrofit onFailure: ", "onFailure: ${t.stackTrace}")

                    binding.swipeRefreshLayout.isRefreshing = false
                }
            })
    }

    private fun cariMahasiswa(query: String){
        this.binding.progressIndicator.visibility = View.GONE
        NetworkConfig().getServices()
            .cariMahasiswa(query)
            .enqueue(object : Callback<ResponseListMahasiswa>{
                override fun onResponse(
                    call: Call<ResponseListMahasiswa>,
                    response: Response<ResponseListMahasiswa>
                ) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    if (response.isSuccessful) {
                        val receiverDatas = response.body()?.data
                        setToAdapter(receiverDatas)
                    }
                }

                override fun onFailure(call: Call<ResponseListMahasiswa>, t: Throwable) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    Log.d("Retrofit onFailure: ", "onFailure: ${t.stackTrace}")

                }

            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val cariItem = menu?.findItem(R.id.app_bar_search)
        val cariView: SearchView = cariItem?.actionView as SearchView
        cariView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Check if query is not null before passing it to cariMahasiswa
                if (query != null) {
                    cariMahasiswa(query)
                } else {
                    // Handle the case where query is null if needed
                    cariMahasiswa("") // or some default value
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return true // Return true to indicate that the menu has been created
    }


    private fun setToAdapter(receiverDatas: List<DataMahasiswa?>?) {
        val adapter = MahasiswaAdapter(receiverDatas)
        val lm = LinearLayoutManager(this)
        binding.rvMahasiswa.layoutManager = lm
        binding.rvMahasiswa.itemAnimator = DefaultItemAnimator()
        binding.rvMahasiswa.adapter = adapter
    }

    override fun onRefresh() {
        getMahasiswa()
    }
}
