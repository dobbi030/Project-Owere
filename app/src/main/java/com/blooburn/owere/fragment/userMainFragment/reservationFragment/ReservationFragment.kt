package com.blooburn.owere.fragment.userMainFragment.reservationFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ReservationLayoutBinding

class   ReservationFragment : Fragment(R.layout.reservation_layout) {



    var binding : ReservationLayoutBinding? = null

    var completedFragment : CompletedFragment? = null
    var scheduledFragment : ScheduledFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reservationLayoutBinding = ReservationLayoutBinding.bind(view)

        //프라그먼트 초기화
        scheduledFragment = ScheduledFragment()
        completedFragment = CompletedFragment()

        binding = reservationLayoutBinding
        val spinner = binding?.reservationSpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.reservation_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner?.adapter = it
        }

        //스피너 선택 리스너
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                when(position){
                    //첫 번째 아이템 선택
                    0 -> requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.reservation_fragment_container, scheduledFragment!!)
                        .commit()

                    //두 번째 아이템 선택
                    1 ->requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.reservation_fragment_container, completedFragment!!)
                        .commit()
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }



}