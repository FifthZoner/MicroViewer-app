package com.fz.microviewerapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fz.microviewerapp.R
import com.fz.microviewerapp.ui.ui.main.CategoryBoardsFragment

class CategoryBoards : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_boards)

        val cat_id = intent.getLongExtra("cat_id", 0);

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CategoryBoardsFragment.newInstance(cat_id))
                .commitNow()
        }
    }
}