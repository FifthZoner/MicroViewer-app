package com.fz.microviewerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fz.microviewerapp.ui.main.ActivityDetailsFragment

class ActivityDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_details)

        val boa_id = intent.getLongExtra("boa_id", 0);

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ActivityDetailsFragment.newInstance(boa_id))
                .commitNow()
        }
    }
}