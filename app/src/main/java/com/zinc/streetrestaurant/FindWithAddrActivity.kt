package com.zinc.streetrestaurant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zinc.streetrestaurant.databinding.ActivityFindWithAddrBinding

class FindWithAddrActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFindWithAddrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_with_addr)

        setUpView()
    }

    private fun setUpView() {

    }
}