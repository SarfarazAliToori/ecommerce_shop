package com.acclivousbyte.shopee.view.activties.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.acclivousbyte.shopee.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //findNavController(R.id.fragmentNavHostContainerView)
    }


//    override fun onSupportNavigateUp(): Boolean {
//        findNavController(R.id.nav_host_fragment_content_main)
//        return super.onSupportNavigateUp()
//    }
}