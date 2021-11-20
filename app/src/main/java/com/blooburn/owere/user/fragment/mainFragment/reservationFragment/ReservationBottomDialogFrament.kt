package com.blooburn.owere.user.fragment.mainFragment.reservationFragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.blooburn.owere.R
import com.blooburn.owere.designer.activity.reservation.RefuseReservation
import com.blooburn.owere.user.activity.main.userReservation.ReservationCanceled
import com.blooburn.owere.user.item.ReservationListItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth


class ReservationBottomDialogFrament : BottomSheetDialogFragment() {

    private var reservation: ReservationListItem? = null//상세페이지에서 전달받을 예약 객체


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.reservation_bottom_dialog, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //디자이너 객체 받기
        reservation = arguments?.getParcelable<Parcelable>("reservation") as ReservationListItem?
        //번들로 하는 거 아님 실수 주의
        //designerData = bundle.getParcelable<Parcelable>(DESIGNER_DATA_KEY) as UserDesignerItem?
        //menu = bundle.getParcelable<Parcelable>("SESLECTED_MENU_DATA_KEY") as StyleMenuItem?

        Log.d("reservation",reservation!!.designerName)


        // 예약 취소 버튼
        var cancelButton = view.findViewById<TextView>(R.id.cancel_reservation)

//기본 alertDialog는 커스텀이 안 되어서 주석 처리 커스텀 다이얼로그 클래스 생성
//        cancelButton.setOnClickListener {
//            var builder = AlertDialog.Builder(requireContext())
//                .setTitle("정말 예약을 취소하실건가요?")
//                .setPositiveButton("네 취소할게요", positiveButtonClick)
//                .setNegativeButton("아니요", negativeButtonClick)
//                .show()
//        }

        cancelButton.setOnClickListener {
            val dialog = ReservationCustomDialog(requireContext(), reservation!!)
            dialog.myDiag(requireContext())
        }



    }


}
//커스텀다이얼로그 (예약을 취소하실 건가요?)
class ReservationCustomDialog(context: Context, reservation: ReservationListItem) {
    //전달하려는 예약객체 정보
    private val reservation : ReservationListItem  = reservation
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

            val dialog = ReservationYesDialog(context,reservation)
            dialog.myDiag(context)
        }

        dialog.show()
    }
}

//커스텀다이얼로그 (예약 취소 사유 선택)
class ReservationYesDialog(context: Context, reservation: ReservationListItem) {
    private val dialog = Dialog(context)

    //전달하려는 예약객체 정보
    private val reservation : ReservationListItem  = reservation

    fun myDiag(context:Context) {
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

        val reasonForCancelFirst = dialog.findViewById<TextView>(R.id.why_cancel_first)
        reasonForCancelFirst.setOnClickListener {  }

        //직접입력 버튼
        val inputItself= dialog.findViewById<TextView>(R.id.why_cancel_inputself)
        inputItself.setOnClickListener {
            val dialog = InputMyselfCustomDialog(context, reservation)
            dialog.myDiag(context)
            
        }

        dialog.show()
    }
}

//커스텀다이얼로그(직접 입력 다이얼로그)
class InputMyselfCustomDialog(context: Context, reservation: ReservationListItem) {

    private val dialog = Dialog(context)

    //전달하려는 예약객체 정보
    private val reservation : ReservationListItem  = reservation

    fun myDiag(context: Context) {
//        보여질 화면 결정
        dialog.setContentView(R.layout.reason_input_cancel_dialog_layout)

        //Dialog 크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //영역 밖 터치 처리
        dialog.setCanceledOnTouchOutside(true)
        //취소 가능
        dialog.setCancelable(true)

        val buttonCancel = dialog.findViewById<TextView>(R.id.button_so_cancel)

        val editTextReason = dialog.findViewById<EditText>(R.id.reason_edittext)

        val textWatcher = object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var length = s.toString().length
                if(length!=0){
                    buttonCancel.visibility = VISIBLE
                }else{
                    buttonCancel.visibility = GONE
                }
            }

        }

        editTextReason.addTextChangedListener(textWatcher)

        //거절하기 버튼 리스너
        buttonCancel.setOnClickListener {

            val intent = Intent(context,RefuseReservation::class.java)
            intent.putExtra("reservation",reservation)//취소하려는 예약 객체 넘겨줌
            intent.putExtra("editTextReason",editTextReason.text) //취소 사유 넘겨줌
            startActivity(context,intent,null)
        }

        dialog.show()
    }
}
