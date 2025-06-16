package com.fz.microviewerapp.ui.categoryBoards

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fz.microviewerapp.R
import com.fz.microviewerapp.ui.categoryBoards.CategoryBoardsFragment

class CategoryBoards : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_boards)

        val cat_id = intent.getLongExtra("cat_id", 0);
        val cat_name = intent.getStringExtra("cat_name");
        if (cat_name != "") setTitle(cat_name)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CategoryBoardsFragment.Companion.newInstance(cat_id))
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}