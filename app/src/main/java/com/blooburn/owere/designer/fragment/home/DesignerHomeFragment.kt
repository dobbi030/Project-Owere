package com.blooburn.owere.designer.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.DesignerHomeFragmentBinding
import com.blooburn.owere.databinding.WaitingFragmentLayoutBinding
import com.blooburn.owere.user.fragment.mainFragment.reservationFragment.WaitingFragment


//디자이너 홈 화면
class DesignerHomeFragment : Fragment(R.layout.designer_home_fragment) {

    private var binding: DesignerHomeFragmentBinding? = null

    private val confirmedReservationFragment: DesignerConfirmedReservationFragment? by lazy{
        DesignerConfirmedReservationFragment()
    }

    //대기중인 고객 예약 리스트
    private val waitingReservationFragment : DesignerWaitingReservationFragment? by lazy {
        DesignerWaitingReservationFragment()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DesignerHomeFragmentBinding.bind(view)



        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar_designer_home)
        initToolbar(toolbar)

        initSpinner()


    }

    override fun onPause() {
        super.onPause()
        if(binding?.spinnerDesignerHome?.selectedItemPosition==1){
            binding?.spinnerDesignerHome?.setSelection(0)
        }else{
            binding?.spinnerDesignerHome?.setSelection(1)
        }

    }





     private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_designer_reservation,
            R.layout.spinner_layout
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerDesignerHome?.adapter = it
        }

         binding?.spinnerDesignerHome?.onItemSelectedListener = spinnerItemSelectedListener


    }

    private val spinnerItemSelectedListener = object : AdapterView.OnItemSelectedListener {


        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {

            when (position) {
                0 -> requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container_designer_home,
                        confirmedReservationFragment ?: return
                    )
                    .commit()

                //예약 수락 대기중 프래그먼트 by 정민 추가
                else ->requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container_designer_home, waitingReservationFragment ?: return
                    )
                    .commit()

            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }


    private fun initToolbar(toolbar: Toolbar?) {
        if (toolbar == null) return

        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.setDisplayShowTitleEnabled(false)  // 앱 타이틀 제거
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