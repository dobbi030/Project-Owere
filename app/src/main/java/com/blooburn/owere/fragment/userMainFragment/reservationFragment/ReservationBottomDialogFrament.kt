package com.blooburn.owere.fragment.userMainFragment.reservationFragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.blooburn.owere.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ReservationBottomDialogFrament : BottomSheetDialogFragment() {

    val positiveButtonClick = { dialogInterface: DialogInterface, i: Int ->
        Toast.makeText(requireContext(), "positive", Toast.LENGTH_SHORT).show()
    }
    val negativeButtonClick = { dialogInterface: DialogInterface, i: Int ->
        Toast.makeText(requireContext(), "negative", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.reservation_bottom_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 예약 취소 버튼
        var cancelButton = view.findViewById<TextView>(R.id.cancel_reservation)
//
//        cancelButton.setOnClickListener {
//            var builder = AlertDialog.Builder(requireContext())
//                .setTitle("정말 예약을 취소하실건가요?")
//                .setPositiveButton("네 취소할게요", positiveButtonClick)
//                .setNegativeButton("아니요", negativeButtonClick)
//                .show()
//        }
        cancelButton.setOnClickListener {
            val dialog = ReservationCustomDialog(requireContext())
            dialog.myDiag(requireContext())
        }



    }


}
//커스텀다이얼로그
class ReservationCustomDialog(context: Context) {
    private val dialog = Dialog(context)

    fun myDiag(context: Context) {
//        보여질 화면 결정
        dialog.setContentView(R.layout.reservation_cancel_dialog_layout)

        //Dialog 크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //영역 밖 터치 처리
        dialog.setCanceledOnTouchOutside(true)
        //취소 가능
        dialog.setCancelable(true)

        val yesButton = dialog.findViewById<TextView>(R.id.reservation_cancel_yes)
        val noButton = dialog.findViewById<TextView>(R.id.reservation_cancel_no)


        yesButton.setOnClickListener {

            val dialog = ReservationYesDialog(context)
            dialog.myDiag()
        }

        dialog.show()
    }
}

//커스텀다이얼로그
class ReservationYesDialog(context: Context) {
    private val dialog = Dialog(context)

    fun myDiag() {
//        보여질 화면 결정
        dialog.setContentView(R.layout.reservation_cancel_yes_layout)

        //Dialog 크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //영역 밖 터치 처리
        dialog.setCanceledOnTouchOutside(true)
        //취소 가능
        dialog.setCancelable(true)



        dialog.show()
    }
}