package com.acclivousbyte.shopee.view.activties

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.view.activties.base.MainActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 2000)
    }
}