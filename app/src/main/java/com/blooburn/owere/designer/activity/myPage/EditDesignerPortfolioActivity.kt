package com.blooburn.owere.designer.activity.myPage

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.designer.adapter.myPage.EditPortfolioListAdapter
import com.blooburn.owere.util.storageInstance


class EditDesignerPortfolioActivity : AppCompatActivity() {

    private val tempDesignerId = "designer0"

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recycler_view_edit_designer_portfolio)
    }
    lateinit var recyclerViewAdapter: EditPortfolioListAdapter

    private val completeBtn: TextView by lazy {
        findViewById(R.id.btn_edit_designer_portfolio_complete)
    }
    private val cancelBtn: TextView by lazy {
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

    private fun initRecyclerView() {
        recyclerViewAdapter = EditPortfolioListAdapter(this)
        recyclerView.apply {
            val sizeOfColumn = 3
            layoutManager = GridLayoutManager(this@EditDesignerPortfolioActivity, sizeOfColumn)
            adapter = recyclerViewAdapter
            addItemDecoration(ItemDecoration(sizeOfColumn))
        }
    }

    private fun initButtons() {
        completeBtn.setOnClickListener { finish() }
        cancelBtn.setOnClickListener { finish() }
    }

    inner class ItemDecoration(private val sizeOfColumn: Int) : RecyclerView.ItemDecoration() {
        private val margin = dpToPx(6) // 여백 6
        private val halfMargin = margin / 2

        // dp -> pixel
        private fun dpToPx(dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                resources.displayMetrics
            )
                .toInt()

        }

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            outRect.bottom = margin

            val position = parent.getChildAdapterPosition(view)
            when (position % sizeOfColumn) {
                0 -> outRect.right = halfMargin
                sizeOfColumn - 1 -> outRect.left = halfMargin
                else -> {
                    outRect.left = halfMargin; outRect.right = halfMargin
                }
            }
        }
    }
}