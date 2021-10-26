package com.blooburn.owere.designer.fragment.home

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.R
import com.blooburn.owere.databinding.DesignerConfirmedReservationFragmentBinding
import com.blooburn.owere.databinding.DesignerHomeFragmentBinding
import com.blooburn.owere.util.CustomDividerDecoration

class DesignerConfirmedReservationFragment :
    Fragment(R.layout.designer_confirmed_reservation_fragment) {

    private var binding: DesignerConfirmedReservationFragmentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DesignerConfirmedReservationFragmentBinding.bind(view)
        // 리사이클러뷰 custom divider 적용
        binding?.recyclerDesignerConfirmedReservationScheduled?.apply {
            addItemDecoration(
                CustomDividerDecoration(1f, ContextCompat.getColor(this.context, R.color.gray_cc))
            )
        }
    }
/*
    private fun initViewPager(){
        binding?.viewPagerUserHome?.apply{
            isSaveEnabled = false
            tabAdapter = UserHomeTabAdapter(
                this@DesignerHomeFragment,
                binding?.tabLayoutUserHome!!.tabCount
            )
            adapter = tabAdapter
        }
    }
*/
}