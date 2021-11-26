package com.blooburn.owere.designer.activity.myPage

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.designer.adapter.myPage.EditPortfolioListAdapter
import com.blooburn.owere.util.storageInstance

class EditDesignerPortfolioActivity : AppCompatActivity() {

    val tempDesignerId = "designer0"

    private val recyclerView: RecyclerView by lazy{
        findViewById(R.id.recycler_view_edit_designer_portfolio)
    }
    lateinit var recyclerViewAdapter: EditPortfolioListAdapter

    private val completeBtn: TextView by lazy{
        findViewById(R.id.btn_edit_designer_portfolio_complete)
    }
    private val cancelBtn: TextView by lazy{
        findViewById(R.id.btn_edit_designer_portfolio_cancel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_designer_portfolio)

        initRecyclerView()
        initButtons()

        storageInstance.reference.child("portfolio/$tempDesignerId")
            .listAll().addOnSuccessListener {
                recyclerViewAdapter.setList(it.items)
                recyclerViewAdapter.notifyDataSetChanged()
            }
    }

    private fun initRecyclerView(){
        recyclerViewAdapter = EditPortfolioListAdapter(this)
        recyclerView.apply{
            layoutManager = GridLayoutManager(this@EditDesignerPortfolioActivity, 3)
            adapter = recyclerViewAdapter
        }
    }

    private fun initButtons(){
        completeBtn.setOnClickListener { finish() }
        cancelBtn.setOnClickListener { finish() }
    }

}