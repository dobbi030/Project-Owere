package com.blooburn.owere.user.activity.main.mypageActivity

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.blooburn.owere.R
import com.google.firebase.auth.FirebaseAuth
//환경설정
class SettingActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

    private val logOutButton by lazy {
        findViewById<TextView>(R.id.setting_logout)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        if(auth.currentUser == null){
            this.finish()
        }



        logOutButton.setOnClickListener {
            val dialog = MemeberQuitDialog(this,auth)
            dialog.myDiag(this,auth)


        }



    }

    //로그아웃 후 finish처리
    override fun onResume() {
        super.onResume()

        if(auth.currentUser== null){
            finish()
        }
    }


}

//커스텀다이얼로그 (로그아웃 하실 건가요?)
class MemeberQuitDialog(context: Context,auth : FirebaseAuth) {
    private val dialog = Dialog(context)

    fun myDiag(context:Context,auth:FirebaseAuth) {
//        보여질 화면 결정
        dialog.setContentView(R.layout.logout_dialog_layout)

        //Dialog 크기 설정
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        //영역 밖 터치 처리
        dialog.setCanceledOnTouchOutside(true)
        //취소 가능
        dialog.setCancelable(true)

        val yesButton = dialog.findViewById<TextView>(R.id.logout_yes)
        val noButton = dialog.findViewById<TextView>(R.id.logout_no)


        yesButton.setOnClickListener {

            auth.signOut()


            //프래그먼트 삭제
            dialog.dismiss()

        }

        dialog.show()
    }
}