package com.blooburn.owere.designer.fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blooburn.owere.R
import com.blooburn.owere.designer.activity.reservation.AcceptReservation
import com.blooburn.owere.designer.item.DesignerReservationDetail

class RefuseCustomDialog(
    context: Context,
    reservation: DesignerReservationDetail,
    dateStamp: Long,
    designerId: String
) {
    private val designerId = designerId
    private val dateStamp = dateStamp
    private val dialog = Dialog(context)

    //전달하려는 예약객체 정보
    private val reservation : DesignerReservationDetail = reservation

    fun myDiag(context: Context) {
//        보여질 화면 결정
        dialog.setContentView(R.layout.reservation_refuse_why_layout)

        //Dialog 크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //영역 밖 터치 처리
        dialog.setCanceledOnTouchOutside(true)
        //취소 가능
        dialog.setCancelable(true)

        val reasonForCancelFirst = dialog.findViewById<TextView>(R.id.why_refuse_first)
        reasonForCancelFirst.setOnClickListener {  }

        //직접입력 버튼
        val inputItself= dialog.findViewById<TextView>(R.id.why_refuse_inputself)
        inputItself.setOnClickListener {
            val dialog = InputMyselfRefuseDialog(context, reservation,dateStamp,designerId)
            dialog.myDiag(context)

        }

        dialog.show()
    }
}

//커스텀다이얼로그(직접 입력 다이얼로그)
class InputMyselfRefuseDialog(
    context: Context,
    reservation: DesignerReservationDetail,
    dateStamp: Long,
    designerId: String
) {
    private val designerId = designerId
    private val dateStamp = dateStamp

    private val dialog = Dialog(context)

    //전달하려는 예약객체 정보
    private val reservation : DesignerReservationDetail = reservation

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

        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var length = s.toString().length
                if(length!=0){
                    buttonCancel.visibility = View.VISIBLE
                }else{
                    buttonCancel.visibility = View.GONE
                }
            }

        }

        editTextReason.addTextChangedListener(textWatcher)

        //거절하기 버튼 리스너
        buttonCancel.setOnClickListener {

            val intent = Intent(context, AcceptReservation::class.java)
            intent.putExtra("reservation",reservation)//취소하려는 예약 객체 넘겨줌
            intent.putExtra("editTextReason",editTextReason.text) //취소 사유 넘겨줌
            intent.putExtra("dateStamp",dateStamp)
            intent.putExtra("designerId",designerId)
            ContextCompat.startActivity(context, intent, null)
        }

        dialog.show()
    }
}
