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

class ReservationFragment : Fragment(R.layout.reservation_layout) {

    val items = arrayOf("예정된 예약", "완료된 예약")

    var binding : ReservationLayoutBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reservationLayoutBinding = ReservationLayoutBinding.bind(view)

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

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }



}